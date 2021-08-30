package com.maetdori.buysomething.service;

import com.maetdori.buysomething.domain.CouponUsed.CouponUsedRepository;
import com.maetdori.buysomething.domain.PointUsed.PointUsed;
import com.maetdori.buysomething.domain.PointUsed.PointUsedRepository;
import com.maetdori.buysomething.web.dto.CouponDto;
import com.maetdori.buysomething.web.dto.PointDto;
import com.maetdori.buysomething.web.dto.UserDto;
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
    @Autowired
    CouponUsedRepository couponUsedRepository;

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
        UserDto.Request user = new UserDto.Request(userName);
        UserDto.Info userBefore = userInfoService.getUserInfo(user);
        userBefore.setCartAmount(cartAmount);
        UserDto.Selection selection = autoSelectService.getSelection(userBefore);

        Long paymentId = makePaymentService.makePayment(selection);

        UserDto.Info userAfter = userInfoService.getUserInfo(user);

        적립금_정상처리_테스트(userBefore.getSavings(), selection.getSavingsToUse(), userAfter.getSavings());
        포인트_정상처리_테스트(userBefore.getUserId(), paymentId, selection.getPointsToUse());
        쿠폰_정상처리_테스트(userBefore.getCoupons().size(), selection.getCouponToUse(), userAfter.getCoupons().size());

    }

    public void 적립금_정상처리_테스트(int savingsBefore, int savingsUsed, int savingsAfter) {
        assertThat(savingsAfter).isEqualTo(savingsBefore - savingsUsed);
    }

    public void 포인트_정상처리_테스트(Long userId, Long paymentId, List<PointDto.Selected> pointSelected) {
        for(PointDto.Selected point: pointSelected) {
            Long pointId = point.getId();
            System.out.println(pointUsedRepository.countAllByUserId(userId));
            PointUsed pointUsed = pointUsedRepository.findByPointIdAndPaymentId(pointId, paymentId);
            assertThat(pointUsed.getAmount()).isEqualTo(point.getAmount());
        }
    }

    public void 쿠폰_정상처리_테스트(int couponsBefore, CouponDto couponUsed, int couponsAfter) {
        if(couponUsed == null) return;
        assertThat(couponsAfter).isEqualTo(couponsBefore-1);
    }
}
