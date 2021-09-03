package com.maetdori.buysomething.service.MakePaymentService;

import com.maetdori.buysomething.domain.Coupon.Coupon;
import com.maetdori.buysomething.domain.Coupon.CouponRepository;
import com.maetdori.buysomething.domain.Payment.Payment;
import com.maetdori.buysomething.domain.Payment.PaymentRepository;
import com.maetdori.buysomething.domain.Point.Point;
import com.maetdori.buysomething.domain.Point.PointRepository;
import com.maetdori.buysomething.domain.PointUsed.PointUsed;
import com.maetdori.buysomething.domain.PointUsed.PointUsedRepository;
import com.maetdori.buysomething.domain.Savings.SavingsRepository;
import com.maetdori.buysomething.domain.SavingsUsed.SavingsUsed;
import com.maetdori.buysomething.domain.SavingsUsed.SavingsUsedRepository;
import com.maetdori.buysomething.domain.User.UserRepository;
import com.maetdori.buysomething.exception.CouponNotFoundException;
import com.maetdori.buysomething.exception.PointNotFoundException;
import com.maetdori.buysomething.util.Percent;
import com.maetdori.buysomething.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MakePaymentServiceImpl implements MakePaymentService {
    private final UserRepository userRepo;
    private final PaymentRepository paymentRepo;
    private final CouponRepository couponRepo;
    private final SavingsRepository savingsRepo;
    private final SavingsUsedRepository savingsUsedRepo;
    private final PointRepository pointRepo;
    private final PointUsedRepository pointUsedRepo;

    @Override
    @Transactional
    public int makePayment(SelectionDto selection) {
        Integer userId = selection.getUserId();
        int cartAmount = selection.getCartAmount();

        SavingsDto savings = selection.getSavingsToUse();
        CouponDto coupon = selection.getCouponToUse();
        List<PointDto> points = selection.getPointsToUse();

        Payment payment = savePayment(userId, cartAmount);

        if(selection.containsCoupon()) useCoupon(coupon, payment);
        if(selection.containsPoints()) {
            for(PointDto point: points) {
                usePoint(point, payment);
            }
        }
        if(selection.containsSavings()) useSavings(savings, payment);

        return payment.getPayAmount();
    }

    @Override
    public Payment savePayment(Integer userId, int cartAmount) {
        return paymentRepo.save(Payment.builder()
                .user(userRepo.findById(userId).get())
                .cartAmount(cartAmount)
                .payAmount(cartAmount)
                .build());
    }

    @Override
    public void useSavings(SavingsDto savingsToUse, Payment payment) {
        //적립금 차감
        int amountToUse = savingsToUse.getAmount();
        savingsRepo.findById(savingsToUse.getId()).get() //check isPresent()
                .useSavings(amountToUse);

        //사용한 적립금 등록
        savingsUsedRepo.save(SavingsUsed.builder()
                .payment(payment)
                .amount(amountToUse)
                .build());

        payment.discount(amountToUse);
    }

    @Override
    public void useCoupon(CouponDto couponToUse, Payment payment) {
        Coupon coupon = couponRepo.findById(couponToUse.getId())
                        .orElseThrow(() -> new CouponNotFoundException());

        coupon.useCoupon(payment);

        payment.discount(Percent.discountAmount(payment.getPayAmount(), couponToUse.getDiscountRate()));
    }

    @Override
    public void usePoint(PointDto pointToUse, Payment payment) {
        Point point = pointRepo.findById(pointToUse.getId())
                .orElseThrow(() -> new PointNotFoundException());

        int amountToUse = pointToUse.getAmount();

        //포인트 차감
        point.usePoint(amountToUse);

        //사용한 포인트 등록
        pointUsedRepo.save(PointUsed.builder()
                .payment(payment)
                .point(point)
                .amount(amountToUse)
                .build());

        payment.discount(amountToUse);
    }
}