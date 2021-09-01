package com.maetdori.buysomething.web.api;

import com.maetdori.buysomething.service.PaymentHistoryService.PaymentHistoryService;
import com.maetdori.buysomething.web.dto.PaymentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class RefundController {
    private final PaymentHistoryService paymentHistoryService;

    @GetMapping("/history")
    public List<PaymentDto> getPaymentHistory(@PathVariable Integer userId) {
        return paymentHistoryService.getPaymentList(userId);
    }
}
