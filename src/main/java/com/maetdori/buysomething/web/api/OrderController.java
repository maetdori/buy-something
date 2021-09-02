package com.maetdori.buysomething.web.api;

import com.maetdori.buysomething.domain.Payment.Payment;
import com.maetdori.buysomething.exception.NoSuchUserException;
import com.maetdori.buysomething.service.AutoSelectService.AutoSelectService;
import com.maetdori.buysomething.service.MakePaymentService.MakePaymentService;
import com.maetdori.buysomething.service.UserInfoService.UserInfoService;
import com.maetdori.buysomething.validation.UserValidation;
import com.maetdori.buysomething.web.dto.SelectionDto;
import com.maetdori.buysomething.web.dto.UserInfoDto;
import com.maetdori.buysomething.web.dto.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class OrderController {
    private final UserInfoService userInfoService;
    private final AutoSelectService autoSelectService;
    private final MakePaymentService makePaymentService;

    private final UserValidation userValidation;

    @PostMapping("/order")
    public UserInfoDto getUserInfo(@RequestBody UserRequest userRequest) throws NoSuchUserException {
        Integer userId = userValidation.getUserIfExist(userRequest).getId();
        return userInfoService.getUserInfo(userId);
    }

    @GetMapping("/order/{userId}/auto-select")
    public SelectionDto getSelection(@PathVariable Integer userId) {
        UserInfoDto userInfo = userInfoService.getUserInfo(userId);
        return autoSelectService.getSelection(userInfo);
    }

    @PostMapping("/order/make-payment")
    public Payment makePayment(@RequestBody SelectionDto selection) {
        return makePaymentService.makePayment(selection);
    }
}
