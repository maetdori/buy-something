package com.maetdori.buysomething.service.PaymentHistoryService;

import com.maetdori.buysomething.domain.Payment.PaymentRepository;
import com.maetdori.buysomething.web.dto.HistoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PaymentHistoryServiceImpl implements PaymentHistoryService {
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<HistoryDto> getPaymentList(Integer userId) {
        return paymentRepository.findAllByUserIdOrderByPurchaseDateDesc(userId).stream()
                .map(HistoryDto::new).collect(Collectors.toList());
    }
}
