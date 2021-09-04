package com.maetdori.buysomething.web.api;

import com.maetdori.buysomething.service.AutoSelectService.AutoSelectService;
import com.maetdori.buysomething.service.MakePaymentService.MakePaymentService;
import com.maetdori.buysomething.service.UserInfoService.UserInfoService;
import com.maetdori.buysomething.web.dto.SelectionDto;
import com.maetdori.buysomething.web.dto.UserInfoDto;
import com.maetdori.buysomething.web.dto.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class OrderController {
    private final UserInfoService userInfoService;
    private final AutoSelectService autoSelectService;
    private final MakePaymentService makePaymentService;

    @PostMapping("/order")
    public UserInfoDto getUserInfo(@RequestBody UserRequest userRequest) {
        return userInfoService.getUserInfo(userRequest);
    }

    @PostMapping("/order/auto-select")
    public SelectionDto getSelection(@RequestBody UserRequest userRequest) {
        UserInfoDto userInfo = userInfoService.getUserInfo(userRequest);
        return autoSelectService.getSelection(userInfo);
    }

    @PostMapping("/order/make-payment")
    public Integer makePayment(@RequestBody SelectionDto selection) {
        return makePaymentService.makePayment(selection, LocalDateTime.now()); //paymentId 반환
    }
}
