package com.maetdori.buysomething.service;

import com.maetdori.buysomething.web.dto.CouponDto;
import com.maetdori.buysomething.web.dto.PointDto;
import com.maetdori.buysomething.web.dto.UserDto;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserInfoServiceTest {
    @Autowired
    UserInfoService userInfoService;

    class TestVal {
        UserDto.Request userReq;
        Expected expects;

        TestVal(UserDto.Request userReq, Expected expects) {
            this.userReq = userReq;
            this.expects = expects;
        }
    }

    class Expected {
        int saving;
        int pointSize;
        int couponSize;

        Expected(int saving, int pointSize, int couponSize) {
            this.saving = saving;
            this.pointSize = pointSize;
            this.couponSize = couponSize;
        }
    }

    @Test
    public void 유저_결제수단_조회_테스트() {
        List<TestVal> tests = new ArrayList<>();

        tests.add(new TestVal(new UserDto.Request(1L,56000), new Expected(1000,3,4)));
        tests.add(new TestVal(new UserDto.Request(2L,78000), new Expected(2300,3,4)));
        tests.add(new TestVal(new UserDto.Request(3L,28000), new Expected(5100,3,2)));
        tests.add(new TestVal(new UserDto.Request(4L,150000), new Expected(4800,4,5)));
        tests.add(new TestVal(new UserDto.Request(5L,12000), new Expected(7000,3,0)));

        for(TestVal testVal: tests)
            validate(testVal.userReq, testVal.expects);
    }

    @Test
    @DisplayName("TEST: 존재하지 않는 회원")
    public void 존재하지_않는_회원() {
        assertThrows(NoSuchElementException.class, () -> {
            UserDto.Request userReq = new UserDto.Request(6L, 50000);
            userInfoService.getUserInfo(userReq);
        });
    }

    public void validate(UserDto.Request userReq, Expected expects) {
        UserDto.Info user = userInfoService.getUserInfo(userReq);

        int savings = user.getSavings();
        List<PointDto> points = user.getPoints();
        List<CouponDto> coupons = user.getCoupons();

        assertThat(savings).isEqualTo(expects.saving);
        assertThat(points.size()).isEqualTo(expects.pointSize);
        for(PointDto point: points) {
            assertThat(point.getExpiryDate()).isAfterOrEqualTo(LocalDate.now());
        }
        assertThat(coupons.size()).isEqualTo(expects.couponSize);
        for(CouponDto coupon: coupons) {
            assertThat(coupon.getMinAmount()).isLessThanOrEqualTo(userReq.getAmount());
        }
    }
}
