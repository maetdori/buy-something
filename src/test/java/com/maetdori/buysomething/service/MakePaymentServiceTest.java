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
import com.maetdori.buysomething.service.UserPayInfoService.UserPayInfoService;
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
    @Autowired UserPayInfoService userPayInfoService;
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

    //?????????, ????????????, ????????????
    Stream<Arguments> makePaymentTester() {
        return Stream.of(
                arguments("andy123", 56000, 43500),
                arguments("ball123", 78000, 55700),
                arguments("camille123", 28000, 16500),
                arguments("daisy123", 150000, 113900),
                arguments("emily123", 12000, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("makePaymentTester")
    void ????????????_??????_?????????(String userName, int cartAmount, int payAmount) {
        UserPayInfoDto userBefore = userPayInfoService.getPayInfo(userName);
        int savingsBefore = userBefore.getSavings().getAmount();
        int pointSizeBefore = userBefore.getPoints().size();
        int couponSizeBefore = userBefore.getCoupons().size();

        SelectionDto selection = autoSelectService.getSelection(userBefore, cartAmount);

        Integer paymentId = makePaymentService.makePayment(selection, LocalDateTime.now());

        ??????_????????????_?????????(paymentRepo.findById(paymentId).get(), payAmount);

        UserPayInfoDto userAfter = userPayInfoService.getPayInfo(userName);

        int savingsAfter = userAfter.getSavings().getAmount();
        int pointSizeAfter = userAfter.getPoints().size();
        int couponSizeAfter = userAfter.getCoupons().size();

        ?????????_????????????_?????????(savingsBefore, selection.getSavingsToUse().getAmount(), savingsAfter);
        ?????????_????????????_?????????(pointSizeBefore, selection.getPointsToUse().size(), pointSizeAfter);
        ??????_????????????_?????????(couponSizeBefore, selection.getCouponToUse(), couponSizeAfter);
    }

    void ??????_????????????_?????????(Payment payment, int payAmount) {
        assertThat(payment.getPayAmount()).isEqualTo(payAmount);
    }

    void ?????????_????????????_?????????(int savingsBefore, int savingsUsed, int savingsAfter) {
        assertThat(savingsAfter)
                .isEqualTo(savingsBefore - savingsUsed);
    }

    void ?????????_????????????_?????????(int pointSizeBefore, int pointSizeUsed, int pointSizeAfter) {
        assertThat(pointSizeAfter)
                .isBetween(pointSizeBefore - pointSizeUsed, pointSizeBefore - pointSizeUsed +1);
    }

    void ??????_????????????_?????????(int couponSizeBefore, CouponDto couponUsed, int couponSizeAfter) {
        if(couponUsed == null) return;
        assertThat(couponSizeAfter).isEqualTo(couponSizeBefore-1);
    }
}
