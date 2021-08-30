package com.maetdori.buysomething.web.api;

import com.maetdori.buysomething.service.PaymentHistoryService;
import com.maetdori.buysomething.web.dto.PaymentDto;
import com.maetdori.buysomething.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class RefundController {
    private final PaymentHistoryService paymentHistoryService;

    @PostMapping("/history")
    public List<PaymentDto.History> getPaymentHistory(@RequestBody Long userId) {
        return paymentHistoryService.getHistory(userId);
    }
}
