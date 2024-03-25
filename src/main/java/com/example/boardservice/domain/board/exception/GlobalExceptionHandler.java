package com.example.boardservice.domain.board.exception;

import com.example.boardservice.domain.board.exception.dto.CommonResponse;
import com.example.boardservice.domain.board.exception.dto.ErrorResponse;
import com.example.boardservice.domain.board.exception.error.*;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
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
    public ResponseEntity<Object> handleUnAuthorizedException() {
        log.error("UnAuthorizedException :: ");

        ErrorCode errorCode = ErrorCode.UnAuthorizedException;

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
     * 게시글을 찾지 못했을 때
     */
    @ExceptionHandler(BoardNotFoundException.class)
    protected ResponseEntity<?> handleBoardNotFoundException() {
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

    /**
     * 게시글태그를 찾을 못했을 경우
     */
    @ExceptionHandler(BoardTagNotFoundException.class)
    protected ResponseEntity<?> handleBoardTagNotFoundException() {
        log.error("handleBoardTagNotFoundException :: ");

        ErrorCode errorCode = ErrorCode.BOARDTAG_NOT_FOUND_EXCEPTION;

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
     * feign error
     */
    @ExceptionHandler(FeignException.Unauthorized.class)
    public ResponseEntity<Object> handleFeignUnauthorizedException(FeignException.Unauthorized ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    /**
     * DuplidateLikeException
     */
    @ExceptionHandler(DuplicateLikeException.class)
    protected ResponseEntity<?> handleDuplicateLikeException() {
        log.error("DuplicateLikeException :: ");

        ErrorCode errorCode = ErrorCode.DUPLIDATE_LIKE_EXCEPTION;

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
     * MemberId 찾지 못했을 경우
     */
    @ExceptionHandler(MemberIdNullOrEmptyException.class)
    protected ResponseEntity<?> handleMemberIdNullOrEmptyException() {
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
    protected ResponseEntity<?> handleBoardCommentNotFoundException() {
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
     * */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<CommonResponse> handleBindException(final BindingResult bindingResult) {
        log.error("handleBindException :: ");
        ErrorCode errorCode = ErrorCode.REQUEST_PARAMETER_BIND_EXCEPTION;


        String defaultMessage = bindingResult.getFieldErrors()
                .get(0)
                .getDefaultMessage();


        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(defaultMessage)
                .build();


        return new ResponseEntity<>(response, errorCode.getStatus());
    }


    /**
     * 유효성검사에 실패하는 경우
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> argumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("argumentNotValidException :: ");

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
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