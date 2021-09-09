package com.maetdori.buysomething.service.MakePaymentService;

import com.maetdori.buysomething.domain.Coupon.Coupon;
import com.maetdori.buysomething.domain.Coupon.CouponRepository;
import com.maetdori.buysomething.domain.Payment.Payment;
import com.maetdori.buysomething.domain.Payment.PaymentRepository;
import com.maetdori.buysomething.domain.Point.Point;
import com.maetdori.buysomething.domain.Point.PointRepository;
import com.maetdori.buysomething.domain.PointUsed.PointUsed;
import com.maetdori.buysomething.domain.PointUsed.PointUsedRepository;
import com.maetdori.buysomething.domain.Savings.Savings;
import com.maetdori.buysomething.domain.Savings.SavingsRepository;
import com.maetdori.buysomething.domain.SavingsUsed.SavingsUsed;
import com.maetdori.buysomething.domain.SavingsUsed.SavingsUsedRepository;
import com.maetdori.buysomething.domain.User.UserRepository;
import com.maetdori.buysomething.exception.Business.EntityNotFound.CouponNotFoundException;
import com.maetdori.buysomething.exception.Business.EntityNotFound.PointNotFoundException;
import com.maetdori.buysomething.exception.Business.EntityNotFound.SavingsNotFoundException;
import com.maetdori.buysomething.util.Percent;
import com.maetdori.buysomething.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
    public Integer makePayment(SelectionDto selection, LocalDateTime purchaseDate) {
        Integer userId = selection.getUserId();
        int cartAmount = selection.getCartAmount();

        Payment payment = createPayment(userId, cartAmount, purchaseDate); //결제정보를 등록

        useSavings(selection, payment); //적립금 사용처리
        useCoupon(selection, payment); //쿠폰 사용처리
        for(PointDto point: selection.getPointsToUse()) { //포인트 사용처리
            usePoint(point, payment);
        }

        return payment.getId();
    }

    @Override
    public Payment createPayment(Integer userId, int cartAmount, LocalDateTime purchaseDate) {
        return paymentRepo.save(Payment.builder()
                .user(userRepo.findById(userId).get())
                .cartAmount(cartAmount)
                .payAmount(cartAmount) //결제금액을 주문금액으로 초기화
                .purchaseDate(purchaseDate)
                .build());
    }

    @Override
    public void useSavings(SelectionDto selection, Payment payment) {
        //사용자가 선택한 할인수단에 적립금이 포함되지 않을 경우
        if(!selection.containsSavings()) return;

        SavingsDto savingsToUse = selection.getSavingsToUse();
        int amountToUse = savingsToUse.getAmount();

        Savings savings = savingsRepo.findById(savingsToUse.getId())
                .orElseThrow(() -> new SavingsNotFoundException());

        savings.verifyUser(payment.getUser().getId()); //유저의 적립금이 맞는지 확인

        //적립금 차감
        savings.useSavings(amountToUse);

        //사용한 적립금 등록
        savingsUsedRepo.save(SavingsUsed.builder()
                .payment(payment)
                .savings(savings)
                .amount(amountToUse)
                .build());

        //할인금액 적용
        payment.discount(amountToUse);
    }

    @Override
    public void useCoupon(SelectionDto selection, Payment payment) {
        //사용자가 선택한 할인수단에 쿠폰이 포함되지 않을 경우
        if(!selection.containsCoupon()) return;

        CouponDto couponToUse = selection.getCouponToUse();

        Coupon coupon = couponRepo.findById(couponToUse.getId())
                        .orElseThrow(() -> new CouponNotFoundException());

        coupon.verifyUser(payment.getUser().getId()); //유저의 쿠폰이 맞는지 확인

        //쿠폰 사용
        coupon.useCoupon(payment);

        //할인금액 적용
        payment.discount(Percent.discountAmount(payment.getCartAmount(), couponToUse.getDiscountRate()));
    }

    @Override
    public void usePoint(PointDto pointToUse, Payment payment) {
        Point point = pointRepo.findById(pointToUse.getId())
                .orElseThrow(() -> new PointNotFoundException());

        int amountToUse = pointToUse.getAmount();

        point.verifyUser(payment.getUser().getId()); //유저의 포인트가 맞는지 확인

        //포인트 차감
        point.usePoint(amountToUse);

        //사용한 포인트 등록
        pointUsedRepo.save(PointUsed.builder()
                .payment(payment)
                .point(point)
                .amount(amountToUse)
                .build());

        //할인금액 적용
        payment.discount(amountToUse);
    }
}