package com.maetdori.buysomething.web.api;

import com.maetdori.buysomething.service.MakeRefundService.MakeRefundService;
import com.maetdori.buysomething.service.PaymentDetailsService.PaymentDetailsService;
import com.maetdori.buysomething.service.PaymentHistoryService.PaymentHistoryService;
import com.maetdori.buysomething.web.dto.PaymentHistoryDto;
import com.maetdori.buysomething.web.dto.PaymentDetailDto;
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
    public List<PaymentHistoryDto> getPaymentHistory(@PathVariable Integer userId) {
        return paymentHistoryService.getPaymentList(userId);
    }

    @GetMapping("/history/{userId}/{paymentId}")
    public PaymentDetailDto getPaymentDetails(@PathVariable Integer userId,
                                              @PathVariable Integer paymentId) {
        return paymentDetailsService.getPaymentDetails(userId, paymentId);
    }

    @GetMapping("/refund/{userId}/{paymentId}")
    public RefundDto makeRefund(@PathVariable Integer userId,
                                @PathVariable Integer paymentId) {
        return makeRefundService.makeRefund(userId, paymentId, LocalDateTime.now());
    }
}
