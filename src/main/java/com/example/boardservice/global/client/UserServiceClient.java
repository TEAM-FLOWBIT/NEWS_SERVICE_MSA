package com.example.boardservice.global.client;

import com.example.boardservice.global.client.dto.MemberInfoByMemberIdResponseDto;
import com.example.boardservice.global.client.dto.MemberInfoResponseDto;
import com.example.boardservice.global.common.CommonResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", configuration = HeaderConfiguration.class)
public interface UserServiceClient {
    //    @PostMapping("/user-service/info")
//    List<ResponseOrder> getOrders(@PathVariable String userId);
    @GetMapping("/api/v1/member/info")
    //@Headers("Content-Type: application/json")
    CommonResDto<MemberInfoResponseDto> getMemberInfo();

    @GetMapping("/api/v1/member/info/{memberId}") // 여기에 memberId를 매핑
    CommonResDto<MemberInfoByMemberIdResponseDto> getMemberInfoByMemberId(@PathVariable("memberId") Long memberId);


}