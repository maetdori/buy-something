package com.maetdori.buysomething.service;

import com.maetdori.buysomething.domain.Payment.PaymentRepository;
import com.maetdori.buysomething.web.dto.PaymentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PaymentHistoryServiceImpl implements PaymentHistoryService {
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public List<PaymentDto.History> getHistory(Long userId) {
        List<PaymentDto.History> history = paymentRepository.findAllByUserIdOrderByPurchaseDateDesc(userId)
                .stream().map(PaymentDto.History::new).collect(Collectors.toList());

        return history;
    }
}
