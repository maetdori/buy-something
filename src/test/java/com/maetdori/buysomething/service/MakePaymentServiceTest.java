package com.maetdori.buysomething.service;

import com.maetdori.buysomething.domain.PointUsed.PointUsedRepository;
import com.maetdori.buysomething.service.AutoSelectService.AutoSelectService;
import com.maetdori.buysomething.service.MakePaymentService.MakePaymentService;
import com.maetdori.buysomething.service.UserInfoService.UserInfoService;
import com.maetdori.buysomething.web.dto.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
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
        UserInfoDto userBefore = userInfoService.getUserInfo(new UserRequest(userName));
        int savingsBefore = userBefore.getSavings().getAmount();
        int pointSizeBefore = userBefore.getPoints().size();
        int couponSizeBefore = userBefore.getCoupons().size();

        userBefore.setCartAmount(cartAmount);
        SelectionDto selection = autoSelectService.getSelection(userBefore);

        makePaymentService.makePayment(selection);

        UserInfoDto userAfter = userInfoService.getUserInfo(new UserRequest(userName));

        int savingsAfter = userAfter.getSavings().getAmount();
        int pointSizeAfter = userAfter.getPoints().size();
        int couponSizeAfter = userAfter.getCoupons().size();

        적립금_정상처리_테스트(savingsBefore, selection.getSavingsToUse().getAmount(), savingsAfter);
        포인트_정상처리_테스트(pointSizeBefore, selection.getPointsToUse().size(), pointSizeAfter);
        쿠폰_정상처리_테스트(couponSizeBefore, selection.getCouponToUse(), couponSizeAfter);

    }

    public void 적립금_정상처리_테스트(int savingsBefore, int savingsUsed, int savingsAfter) {
        assertThat(savingsAfter)
                .isEqualTo(savingsBefore - savingsUsed);
    }

    public void 포인트_정상처리_테스트(int pointSizeBefore, int pointSizeUsed, int pointSizeAfter) {
        assertThat(pointSizeAfter)
                .isBetween(pointSizeBefore - pointSizeUsed, pointSizeBefore - pointSizeUsed +1);
    }

    public void 쿠폰_정상처리_테스트(int couponSizeBefore, CouponDto couponUsed, int couponSizeAfter) {
        if(couponUsed == null) return;
        assertThat(couponSizeAfter).isEqualTo(couponSizeBefore-1);
    }
}
