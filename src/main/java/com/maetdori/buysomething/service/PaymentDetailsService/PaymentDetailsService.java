package com.maetdori.buysomething.service.PaymentDetailsService;

import com.maetdori.buysomething.web.dto.PaymentDetailDto;

public interface PaymentDetailsService {
    PaymentDetailDto getPaymentDetails(Integer userId, Integer paymentId);
}
