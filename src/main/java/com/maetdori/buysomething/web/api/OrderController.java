package com.maetdori.buysomething.web.api;

import com.maetdori.buysomething.domain.Payment.Payment;
import com.maetdori.buysomething.exception.NoSuchUserException;
import com.maetdori.buysomething.service.AutoSelectService.AutoSelectService;
import com.maetdori.buysomething.service.MakePaymentService.MakePaymentService;
import com.maetdori.buysomething.service.UserInfoService.UserInfoService;
import com.maetdori.buysomething.validation.UserValidation;
import com.maetdori.buysomething.web.dto.Selection;
import com.maetdori.buysomething.web.dto.UserInfo;
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
    public UserInfo getUserInfo(@RequestBody UserRequest userRequest) throws NoSuchUserException {
        Integer userId = userValidation.getUserIfExist(userRequest).getId();
        return userInfoService.getUserInfo(userId);
    }

    @GetMapping("/order/auto-select")
    public Selection getSelection(@PathVariable Integer userId) {
        UserInfo userInfo = userInfoService.getUserInfo(userId);
        return autoSelectService.getSelection(userInfo);
    }

    @PostMapping("/order/make-payment")
    public Payment makePayment(@RequestBody Selection selection) {
        return makePaymentService.makePayment(selection);
    }
}
