package com.maetdori.buysomething.service;

import com.maetdori.buysomething.domain.Coupon.CouponRepository;
import com.maetdori.buysomething.domain.CouponUsed.CouponUsed;
import com.maetdori.buysomething.domain.CouponUsed.CouponUsedRepository;
import com.maetdori.buysomething.domain.Payment.Payment;
import com.maetdori.buysomething.domain.Payment.PaymentRepository;
import com.maetdori.buysomething.domain.Point.Point;
import com.maetdori.buysomething.domain.Point.PointRepository;
import com.maetdori.buysomething.domain.PointUsed.PointUsed;
import com.maetdori.buysomething.domain.PointUsed.PointUsedRepository;
import com.maetdori.buysomething.domain.User.UserRepository;
import com.maetdori.buysomething.web.dto.CouponDto;
import com.maetdori.buysomething.web.dto.PointDto;
import com.maetdori.buysomething.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MakePaymentServiceImpl implements MakePaymentService {
    private final UserRepository userRepo;
    private final CouponRepository couponRepo;
    private final PointRepository pointRepo;
    private final PaymentRepository paymentRepo;
    private final CouponUsedRepository couponUsedRepo;
    private final PointUsedRepository pointUsedRepo;

    @Override
    @Transactional
    public Long makePayment(UserDto.Selection selection) {
        Long userId = selection.getUserId();
        Long paymentId = savePayment(selection.getUserId(), selection.getSavingsToUse());
        int savingsToUse = selection.getSavingsToUse();
        CouponDto coupon = selection.getCouponToUse();
        List<PointDto.Selected> points = selection.getPointsToUse();

        if(savingsToUse > 0) useSavings(userId, savingsToUse);
        if(coupon != null) useCoupon(userId, paymentId, coupon);
        if(!points.isEmpty()) {
            for(PointDto.Selected pointToUse: points) {
                usePoint(userId, pointToUse.getId(), paymentId, pointToUse.getAmount());
            }
        }
        return paymentId;
    }

    @Override
    @Transactional
    public Long savePayment(Long userId, int savingsToUse) {
        return paymentRepo.save(Payment.builder()
                .userId(userId)
                .savingsUsed(savingsToUse)
                .purchaseDate(LocalDateTime.now())
                .build()).getId();
    }

    @Override
    @Transactional
    public void useSavings(Long userId, int savingsToUse) {
        userRepo.getById(userId).useSavings(savingsToUse);
    }

    @Override
    @Transactional
    public void useCoupon(Long userId, Long paymentId, CouponDto coupon) {
        couponUsedRepo.save(CouponUsed.builder()
                .userId(userId)
                .paymentId(paymentId)
                .name(coupon.getName())
                .discountRate(coupon.getDiscountRate())
                .minAmount(coupon.getMinAmount())
                .build());

        couponRepo.deleteById(coupon.getId());
    }

    @Override
    @Transactional
    public void usePoint(Long userId, Long pointId, Long paymentId, int pointToUse) {
        pointUsedRepo.save(PointUsed.builder()
                .userId(userId)
                .pointId(pointId)
                .paymentId(paymentId)
                .amount(pointToUse)
                .build());

        Point point = pointRepo.getById(pointId);
        int maxUse = point.getAmount();
        if(pointToUse == maxUse) { //해당 포인트를 전부 사용할 경우
            pointRepo.deleteById(pointId); //포인트 테이블에서 해당 포인트를 삭제한다.
            return;
        }
        point.usePoint(pointToUse); //포인트를 차감한다.
    }
}