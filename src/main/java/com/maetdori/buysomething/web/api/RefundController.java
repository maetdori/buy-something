package com.maetdori.buysomething.web.api;

import com.maetdori.buysomething.service.MakeRefundService.MakeRefundService;
import com.maetdori.buysomething.service.PaymentDetailsService.PaymentDetailsService;
import com.maetdori.buysomething.service.PaymentHistoryService.PaymentHistoryService;
import com.maetdori.buysomething.web.dto.HistoryDto;
import com.maetdori.buysomething.web.dto.PaymentDto;
import com.maetdori.buysomething.web.dto.RefundDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class RefundController {
    private final PaymentHistoryService paymentHistoryService;
    private final PaymentDetailsService paymentDetailsService;
    private final MakeRefundService makeRefundService;

    @GetMapping("/history/{userId}")
    public List<HistoryDto> getPaymentHistory(@PathVariable(name = "userId") Integer userId) {
        return paymentHistoryService.getPaymentList(userId);
    }

    @GetMapping("/history/{userId}/{paymentId}")
    public PaymentDto getPaymentDetails(@PathVariable(name = "userId") Integer userId,
                                        @PathVariable(name = "paymentId") Integer paymentId) {
        return paymentDetailsService.getPaymentDetails(userId, paymentId);
    }

    @GetMapping("refund/{paymentId}")
    public RefundDto makeRefund(@PathVariable(name = "userId") Integer userId,
                                @PathVariable(name = "paymentId") Integer paymentId) {
        return makeRefundService.makeRefund(userId, paymentId, LocalDateTime.now());
    }
}
