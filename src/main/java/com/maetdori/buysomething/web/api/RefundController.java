package com.maetdori.buysomething.web.api;

import com.maetdori.buysomething.service.PaymentDetailsService.PaymentDetailsService;
import com.maetdori.buysomething.service.PaymentHistoryService.PaymentHistoryService;
import com.maetdori.buysomething.web.dto.HistoryDto;
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
    private final PaymentDetailsService paymentDetailsService;

    @GetMapping("/history/{userId}")
    public List<HistoryDto> getPaymentHistory(@PathVariable(name = "userId") Integer userId) {
        return paymentHistoryService.getPaymentList(userId);
    }

    @GetMapping("/history/{userId}/{paymentId}") //매핑 어떻게 해야 하는지?
    public PaymentDto getPaymentDetails(@PathVariable(name = "paymentId") Integer paymentId) {
        return paymentDetailsService.getPaymentDetails(paymentId);
    }
}
