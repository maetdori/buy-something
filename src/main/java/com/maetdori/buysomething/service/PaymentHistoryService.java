package com.maetdori.buysomething.service;

import com.maetdori.buysomething.web.dto.PaymentDto;

import java.util.List;

public interface PaymentHistoryService {
    List<PaymentDto.History> getHistory(Long userId);
}
