package com.maetdori.buysomething.service.PaymentDetailsService;

import com.maetdori.buysomething.web.dto.PaymentDto;

import java.util.List;

public interface PaymentDetailsService {
    PaymentDto getPaymentDetails(Integer paymentId);
}
