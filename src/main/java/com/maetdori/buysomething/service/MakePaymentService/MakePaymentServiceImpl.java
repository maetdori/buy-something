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

        Payment payment = createPayment(userId, cartAmount, purchaseDate); //??????????????? ??????

        useSavings(selection, payment); //????????? ????????????
        useCoupon(selection, payment); //?????? ????????????
        for(PointDto point: selection.getPointsToUse()) { //????????? ????????????
            usePoint(point, payment);
        }

        return payment.getId();
    }

    @Override
    public Payment createPayment(Integer userId, int cartAmount, LocalDateTime purchaseDate) {
        return paymentRepo.save(Payment.builder()
                .user(userRepo.findById(userId).get())
                .cartAmount(cartAmount)
                .payAmount(cartAmount) //??????????????? ?????????????????? ?????????
                .purchaseDate(purchaseDate)
                .build());
    }

    @Override
    public void useSavings(SelectionDto selection, Payment payment) {
        //???????????? ????????? ??????????????? ???????????? ???????????? ?????? ??????
        if(!selection.containsSavings()) return;

        SavingsDto savingsToUse = selection.getSavingsToUse();
        int amountToUse = savingsToUse.getAmount();

        Savings savings = savingsRepo.findById(savingsToUse.getId())
                .orElseThrow(() -> new SavingsNotFoundException());

        savings.verifyUser(payment.getUser().getId()); //????????? ???????????? ????????? ??????

        //????????? ??????
        savings.useSavings(amountToUse);

        //????????? ????????? ??????
        savingsUsedRepo.save(SavingsUsed.builder()
                .payment(payment)
                .savings(savings)
                .amount(amountToUse)
                .build());

        //???????????? ??????
        payment.discount(amountToUse);
    }

    @Override
    public void useCoupon(SelectionDto selection, Payment payment) {
        //???????????? ????????? ??????????????? ????????? ???????????? ?????? ??????
        if(!selection.containsCoupon()) return;

        CouponDto couponToUse = selection.getCouponToUse();

        Coupon coupon = couponRepo.findById(couponToUse.getId())
                        .orElseThrow(() -> new CouponNotFoundException());

        coupon.verifyUser(payment.getUser().getId()); //????????? ????????? ????????? ??????

        //?????? ??????
        coupon.useCoupon(payment);

        //???????????? ??????
        payment.discount(Percent.discountAmount(payment.getCartAmount(), couponToUse.getDiscountRate()));
    }

    @Override
    public void usePoint(PointDto pointToUse, Payment payment) {
        Point point = pointRepo.findById(pointToUse.getId())
                .orElseThrow(() -> new PointNotFoundException());

        int amountToUse = pointToUse.getAmount();

        point.verifyUser(payment.getUser().getId()); //????????? ???????????? ????????? ??????

        //????????? ??????
        point.usePoint(amountToUse);

        //????????? ????????? ??????
        pointUsedRepo.save(PointUsed.builder()
                .payment(payment)
                .point(point)
                .amount(amountToUse)
                .build());

        //???????????? ??????
        payment.discount(amountToUse);
    }
}