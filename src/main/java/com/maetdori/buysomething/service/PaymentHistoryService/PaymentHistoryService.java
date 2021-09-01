package com.maetdori.buysomething.service.PaymentHistoryService;

import com.maetdori.buysomething.web.dto.PaymentDto;

import java.util.List;

public interface PaymentHistoryService {
    List<PaymentDto> getPaymentList(Integer userId);
}
