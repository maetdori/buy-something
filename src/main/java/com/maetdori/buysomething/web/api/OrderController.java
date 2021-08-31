package com.maetdori.buysomething.web.api;

import com.maetdori.buysomething.domain.Payment.Payment;
import com.maetdori.buysomething.exception.NoSuchUserException;
import com.maetdori.buysomething.service.AutoSelectService;
import com.maetdori.buysomething.service.MakePaymentService;
import com.maetdori.buysomething.service.UserInfoService;
import com.maetdori.buysomething.web.dto.Selection;
import com.maetdori.buysomething.web.dto.UserInfo;
import com.maetdori.buysomething.web.dto.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OrderController {
    private final UserInfoService userInfoService;
    private final AutoSelectService autoSelectService;
    private final MakePaymentService makePaymentService;

    @PostMapping("/order")
    public UserInfo getUserInfo(@RequestBody UserRequest userRequest) throws NoSuchUserException {
        return userInfoService.getUserInfo(userRequest);
    }

    @PostMapping("/order/auto-select")
    public Selection getSelection(@RequestBody UserInfo userInfo) {
        return autoSelectService.getSelection(userInfo);
    }

    @PostMapping("/order/make-payment")
    public Payment makePayment(@RequestBody Selection selection) {
        return makePaymentService.makePayment(selection);
    }
}
