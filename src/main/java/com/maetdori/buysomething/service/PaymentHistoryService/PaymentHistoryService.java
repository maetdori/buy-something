package com.maetdori.buysomething.service.PaymentHistoryService;

import com.maetdori.buysomething.web.dto.HistoryDto;

import java.util.List;

public interface PaymentHistoryService {
    List<HistoryDto> getPaymentList(Integer userId);
}
