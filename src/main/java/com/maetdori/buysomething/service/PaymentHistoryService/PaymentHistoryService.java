package com.maetdori.buysomething.service.PaymentHistoryService;

import com.maetdori.buysomething.web.dto.PaymentHistoryDto;

import java.util.List;

public interface PaymentHistoryService {
    List<PaymentHistoryDto> getPaymentList(Integer userId);
}
