package com.maetdori.buysomething.service;

import com.maetdori.buysomething.domain.PointUsed.PointUsedRepository;
import com.maetdori.buysomething.web.dto.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
public class MakePaymentServiceTest {
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    AutoSelectService autoSelectService;
    @Autowired
    MakePaymentService makePaymentService;
    @Autowired
    PointUsedRepository pointUsedRepository;

    //사용자이름, 주문금액
    static Stream<Arguments> makePaymentTester() {
        return Stream.of(
                arguments("andy123", 56000),
                arguments("ball123", 78000),
                arguments("camille123", 28000),
                arguments("daisy123", 150000),
                arguments("emily123", 12000)
        );
    }

    @ParameterizedTest
    @Transactional
    @MethodSource("makePaymentTester")
    public void 결제요청_처리_테스트(String userName, int cartAmount) {
        UserRequest user = new UserRequest(userName);
        UserInfo userBefore = userInfoService.getUserInfo(user);
        userBefore.setCartAmount(cartAmount);
        Selection selection = autoSelectService.getSelection(userBefore);

        makePaymentService.makePayment(selection);

        UserInfo userAfter = userInfoService.getUserInfo(user);

        적립금_정상처리_테스트(userBefore.getSavings(), selection.getSavingsToUse(), userAfter.getSavings());
        포인트_정상처리_테스트(userBefore.getPoints(), selection.getPointsToUse(), userAfter.getPoints());
        쿠폰_정상처리_테스트(userBefore.getCoupons(), selection.getCouponToUse(), userAfter.getCoupons());

    }

    public void 적립금_정상처리_테스트(SavingsDto savingsBefore, SavingsDto savingsUsed, SavingsDto savingsAfter) {
        assertThat(savingsAfter.getAmount())
                .isEqualTo(savingsBefore.getAmount() - savingsUsed.getAmount());
    }

    public void 포인트_정상처리_테스트(List<PointDto> pointsBefore, List<PointDto> pointsUsed, List<PointDto> pointsAfter) {
        assertThat(pointsAfter.size())
                .isBetween(pointsBefore.size() - pointsUsed.size(),
                        pointsBefore.size() - pointsUsed.size() +1);
    }

    public void 쿠폰_정상처리_테스트(List<CouponDto> couponsBefore, CouponDto couponUsed, List<CouponDto> couponsAfter) {
        if(couponUsed == null) return;
        assertThat(couponsAfter.size()).isEqualTo(couponsBefore.size()-1);
    }
}
