package com.maetdori.buysomething.service.PaymentHistoryService;

import com.maetdori.buysomething.domain.Payment.PaymentRepository;
import com.maetdori.buysomething.web.dto.PaymentHistoryDto;
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
    public List<PaymentHistoryDto> getPaymentList(Integer userId) {
        //날짜 기준 내림차순 정렬된 결제이력 반환
        return paymentRepository.findAllByUserIdOrderByPurchaseDateDesc(userId).stream()
                .map(PaymentHistoryDto::new).collect(Collectors.toList());
    }
}
