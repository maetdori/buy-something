package com.maetdori.buysomething.service.PaymentDetailsService;

import com.maetdori.buysomething.domain.Coupon.CouponRepository;
import com.maetdori.buysomething.domain.Payment.Payment;
import com.maetdori.buysomething.domain.Payment.PaymentRepository;
import com.maetdori.buysomething.domain.PointUsed.PointUsedRepository;
import com.maetdori.buysomething.domain.SavingsUsed.SavingsUsedRepository;
import com.maetdori.buysomething.web.dto.CouponDto;
import com.maetdori.buysomething.web.dto.PaymentDto;
import com.maetdori.buysomething.web.dto.PointDto;
import com.maetdori.buysomething.web.dto.SavingsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PaymentDetailsServiceImpl implements PaymentDetailsService {
    private final CouponRepository couponRepo;
    private final PointUsedRepository pointUsedRepo;
    private final SavingsUsedRepository savingsUsedRepo;
    private final PaymentRepository paymentRepo;

    @Override
    @Transactional(readOnly = true)
    public PaymentDto getPaymentDetails(Integer paymentId) {
        Payment payment = paymentRepo.findById(paymentId).get(); //check isPresent()

        int cartAmount = payment.getCartAmount();
        CouponDto usedCoupon = new CouponDto(couponRepo.findByPaymentId(paymentId));
        List<PointDto> usedPoints = pointUsedRepo.findAllByPaymentId(paymentId).stream()
                .map(PointDto::new).collect(Collectors.toList());
        SavingsDto usedSavings = new SavingsDto(savingsUsedRepo.findByPaymentId(paymentId));

        return PaymentDto.builder()
                .cartAmount(cartAmount)
                .coupon(usedCoupon)
                .points(usedPoints)
                .savings(usedSavings)
                .build();
    }
}
