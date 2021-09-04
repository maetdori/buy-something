package com.maetdori.buysomething.web.api;

import com.maetdori.buysomething.service.AutoSelectService.AutoSelectService;
import com.maetdori.buysomething.service.MakePaymentService.MakePaymentService;
import com.maetdori.buysomething.service.UserPayInfoService.UserPayInfoService;
import com.maetdori.buysomething.web.dto.SelectionDto;
import com.maetdori.buysomething.web.dto.UserPayInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class OrderController {
    private final UserPayInfoService userPayInfoService;
    private final AutoSelectService autoSelectService;
    private final MakePaymentService makePaymentService;

    @GetMapping("/order/{userName}")
    public UserPayInfoDto getUserPayInfo(@PathVariable String userName) {
        return userPayInfoService.getPayInfo(userName);
    }

    @GetMapping("/order/auto-select/{userId}/{cartAmount}")
    public SelectionDto getSelection(@PathVariable Integer userId,
                                     @PathVariable int cartAmount) {
        UserPayInfoDto userPayInfo = userPayInfoService.getPayInfo(userId);
        return autoSelectService.getSelection(userPayInfo, cartAmount);
    }

    @PostMapping("/order/payment")
    public Integer makePayment(@RequestBody SelectionDto selection) {
        return makePaymentService.makePayment(selection, LocalDateTime.now()); //paymentId 반환
    }
}
