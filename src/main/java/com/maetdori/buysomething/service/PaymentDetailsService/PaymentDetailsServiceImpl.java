package com.maetdori.buysomething.service.PaymentDetailsService;

import com.maetdori.buysomething.domain.Coupon.Coupon;
import com.maetdori.buysomething.domain.Coupon.CouponRepository;
import com.maetdori.buysomething.domain.Payment.Payment;
import com.maetdori.buysomething.domain.Payment.PaymentRepository;
import com.maetdori.buysomething.domain.PointUsed.PointUsedRepository;
import com.maetdori.buysomething.domain.SavingsUsed.SavingsUsed;
import com.maetdori.buysomething.domain.SavingsUsed.SavingsUsedRepository;
import com.maetdori.buysomething.exception.Business.EntityNotFound.PaymentNotFoundException;
import com.maetdori.buysomething.web.dto.CouponDto;
import com.maetdori.buysomething.web.dto.PaymentDetailDto;
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
    public PaymentDetailDto getPaymentDetails(Integer userId, Integer paymentId) {
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException());

        payment.verifyUser(userId); //유저의 결제내역이 맞는지 확인

        PaymentDetailDto paymentDto = new PaymentDetailDto();

        paymentDto.setCartAmount(payment.getCartAmount());
        paymentDto.setPayAmount(payment.getPayAmount());

        Coupon coupon = couponRepo.findByPaymentId(paymentId).orElse(null);
        if(coupon!=null) paymentDto.setUsedCoupon(new CouponDto(coupon));

        List<PointDto> usedPoints = pointUsedRepo.findAllByPaymentId(paymentId).stream()
                .map(PointDto::new).collect(Collectors.toList());
        paymentDto.setUsedPoints(usedPoints);

        SavingsUsed savingsUsed = savingsUsedRepo.findByPaymentId(paymentId).orElse(null);
        if(savingsUsed!=null) paymentDto.setUsedSavings(new SavingsDto(savingsUsed));

        return paymentDto;
    }
}
