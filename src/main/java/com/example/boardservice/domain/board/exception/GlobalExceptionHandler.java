package com.example.boardservice.domain.board.exception;

import com.example.boardservice.domain.board.exception.dto.CommonResponse;
import com.example.boardservice.domain.board.exception.dto.ErrorResponse;
import com.example.boardservice.domain.board.exception.error.BoardCommentNotFoundException;
import com.example.boardservice.domain.board.exception.error.BoardNotFoundException;
import com.example.boardservice.domain.board.exception.error.MemberIdNullOrEmptyException;
import com.example.boardservice.domain.board.exception.error.UnAuthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.net.BindException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException ex) {
        if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
        }
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<Object> handleUnAuthorizedException(UnAuthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    /**
     * 게시글을 찾지 못했을 때
     */
    @ExceptionHandler(BoardNotFoundException.class)
    protected ResponseEntity<?> handleBoardNotFoundException(BoardNotFoundException ex) {
        log.error("handleBoardNotFoundException :: ");

        ErrorCode errorCode = ErrorCode.BOARD_NOT_FOUND_EXCEPTION;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }


    @ExceptionHandler(MemberIdNullOrEmptyException.class)
    protected ResponseEntity<?> handleMemberIdNullOrEmptyException(MemberIdNullOrEmptyException ex) {
        log.error("MemberIdNullOrEmptyException :: ");

        ErrorCode errorCode = ErrorCode.MemberIdNullOrEmptyException;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    /**
     * 게시글 댓글을 찾지 못했을 때
     */
    @ExceptionHandler(BoardCommentNotFoundException.class)
    protected ResponseEntity<?> handleBoardCommentNotFoundException(BoardCommentNotFoundException ex) {
        log.error("BoardCommentNotFoundException :: ");

        ErrorCode errorCode = ErrorCode.BOARDCOMMNET_NOT_FOUND_EXCEPTION;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }
    /**
     * 리퀘스트 파라미터 바인딩이 실패했을때
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<CommonResponse> handleRequestParameterBindException(BindException ex) {
        log.error("handleRequestParameterBindException :: ");

        ErrorCode errorCode = ErrorCode.REQUEST_PARAMETER_BIND_EXCEPTION;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }


    /**
     *
     * 유효성검사에 실패하는
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> argumentNotValidException(BindingResult bindingResult,MethodArgumentNotValidException ex) {
        log.error("argumentNotValidException :: ");
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        ErrorCode errorCode = ErrorCode.REQUEST_PARAMETER_BIND_EXCEPTION;

        List<String> errorMessages = fieldErrors.stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());
        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorMessages.toString())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }


    /**
     *  주로 파일 업로드나 멀티파트 요청에서 파트나 매개변수가 누락된 경우에 해당 예외
     */

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<?> missingServletRequestPartException(MissingServletRequestPartException exception) {
        log.error("MissingServletRequestPartException = {}", exception);
        return ResponseEntity.badRequest().body("MissingServletRequestPartException");
    }
    /**
     *
     * 유효성검사 타입 불일치
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        log.error("MethodArgumentTypeMismatchException = {}", exception);
        return ResponseEntity.badRequest().body("잘못된 형식의 값입니다.");
    }
}