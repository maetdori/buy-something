package com.maetdori.buysomething.service;

import com.maetdori.buysomething.SampleDataInput;
import com.maetdori.buysomething.domain.Coupon.CouponRepository;
import com.maetdori.buysomething.domain.Payment.Payment;
import com.maetdori.buysomething.domain.Payment.PaymentRepository;
import com.maetdori.buysomething.domain.Point.PointRepository;
import com.maetdori.buysomething.domain.PointUsed.PointUsedRepository;
import com.maetdori.buysomething.domain.Savings.SavingsRepository;
import com.maetdori.buysomething.domain.SavingsUsed.SavingsUsedRepository;
import com.maetdori.buysomething.domain.User.UserRepository;
import com.maetdori.buysomething.service.AutoSelectService.AutoSelectService;
import com.maetdori.buysomething.service.MakePaymentService.MakePaymentService;
import com.maetdori.buysomething.service.UserInfoService.UserInfoService;
import com.maetdori.buysomething.web.dto.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class MakePaymentServiceTest {
    @Autowired UserInfoService userInfoService;
    @Autowired AutoSelectService autoSelectService;
    @Autowired MakePaymentService makePaymentService;
    @Autowired UserRepository userRepo;
    @Autowired SavingsRepository savingsRepo;
    @Autowired CouponRepository couponRepo;
    @Autowired PointRepository pointRepo;
    @Autowired PaymentRepository paymentRepo;
    @Autowired SavingsUsedRepository savingsUsedRepo;
    @Autowired PointUsedRepository pointUsedRepo;

    @BeforeAll
    void setup() {
        new SampleDataInput(userRepo, savingsRepo, couponRepo, pointRepo);
    }

    @AfterAll
    void clear() {
        savingsUsedRepo.deleteAll();
        pointUsedRepo.deleteAll();
        savingsRepo.deleteAll();
        pointRepo.deleteAll();
        couponRepo.deleteAll();
        paymentRepo.deleteAll();
        userRepo.deleteAll();
    }

    //사용자, 주문금액, 결제금액
    Stream<Arguments> makePaymentTester() {
        return Stream.of(
                arguments(new UserRequest("andy123"), 56000, 43500),
                arguments(new UserRequest("ball123"), 78000, 55700),
                arguments(new UserRequest("camille123"), 28000, 16500),
                arguments(new UserRequest("daisy123"), 150000, 113900),
                arguments(new UserRequest("emily123"), 12000, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("makePaymentTester")
    void 결제요청_처리_테스트(UserRequest user, int cartAmount, int payAmount) {
        UserInfoDto userBefore = userInfoService.getUserInfo(user);
        int savingsBefore = userBefore.getSavings().getAmount();
        int pointSizeBefore = userBefore.getPoints().size();
        int couponSizeBefore = userBefore.getCoupons().size();

        userBefore.setCartAmount(cartAmount);
        SelectionDto selection = autoSelectService.getSelection(userBefore);

        Integer paymentId = makePaymentService.makePayment(selection, LocalDateTime.now());

        할인_정상처리_테스트(paymentRepo.findById(paymentId).get(), payAmount);

        UserInfoDto userAfter = userInfoService.getUserInfo(user);

        int savingsAfter = userAfter.getSavings().getAmount();
        int pointSizeAfter = userAfter.getPoints().size();
        int couponSizeAfter = userAfter.getCoupons().size();

        적립금_정상처리_테스트(savingsBefore, selection.getSavingsToUse().getAmount(), savingsAfter);
        포인트_정상처리_테스트(pointSizeBefore, selection.getPointsToUse().size(), pointSizeAfter);
        쿠폰_정상처리_테스트(couponSizeBefore, selection.getCouponToUse(), couponSizeAfter);
    }

    void 할인_정상처리_테스트(Payment payment, int payAmount) {
        assertThat(payment.getPayAmount()).isEqualTo(payAmount);
    }

    void 적립금_정상처리_테스트(int savingsBefore, int savingsUsed, int savingsAfter) {
        assertThat(savingsAfter)
                .isEqualTo(savingsBefore - savingsUsed);
    }

    void 포인트_정상처리_테스트(int pointSizeBefore, int pointSizeUsed, int pointSizeAfter) {
        assertThat(pointSizeAfter)
                .isBetween(pointSizeBefore - pointSizeUsed, pointSizeBefore - pointSizeUsed +1);
    }

    void 쿠폰_정상처리_테스트(int couponSizeBefore, CouponDto couponUsed, int couponSizeAfter) {
        if(couponUsed == null) return;
        assertThat(couponSizeAfter).isEqualTo(couponSizeBefore-1);
    }
}
