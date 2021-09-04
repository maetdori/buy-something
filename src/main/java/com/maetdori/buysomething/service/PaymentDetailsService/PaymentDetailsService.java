package com.maetdori.buysomething.service.PaymentDetailsService;

import com.maetdori.buysomething.web.dto.PaymentDto;

public interface PaymentDetailsService {
    PaymentDto getPaymentDetails(Integer userId, Integer paymentId);
}
