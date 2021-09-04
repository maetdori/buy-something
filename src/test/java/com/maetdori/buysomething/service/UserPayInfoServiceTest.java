package com.maetdori.buysomething.service;

import com.maetdori.buysomething.SampleDataInput;
import com.maetdori.buysomething.domain.Coupon.CouponRepository;
import com.maetdori.buysomething.domain.Point.PointRepository;
import com.maetdori.buysomething.domain.Savings.SavingsRepository;
import com.maetdori.buysomething.domain.User.UserRepository;
import com.maetdori.buysomething.exception.Business.EntityNotFound.UserNotFoundException;
import com.maetdori.buysomething.service.UserPayInfoService.UserPayInfoService;
import com.maetdori.buysomething.web.dto.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class UserPayInfoServiceTest {
    @Autowired UserPayInfoService userPayInfoService;
    @Autowired UserRepository userRepo;
    @Autowired SavingsRepository savingsRepo;
    @Autowired CouponRepository couponRepo;
    @Autowired PointRepository pointRepo;

    @BeforeAll
    void setup() {
        new SampleDataInput(userRepo, savingsRepo, couponRepo, pointRepo);
    }

    @AfterAll
    void clear() {
        savingsRepo.deleteAll();
        couponRepo.deleteAll();
        pointRepo.deleteAll();
        userRepo.deleteAll();
    }

    class Expected {
        int savings;
        int pointSize;
        int couponSize;

        Expected(int savings, int pointSize, int couponSize) {
            this.savings = savings;
            this.pointSize = pointSize;
            this.couponSize = couponSize;
        }
    }

    Stream<Arguments> nameAndExpectedProvider() {
        return Stream.of(
                arguments("andy123", 1000, 3, 2),
                arguments("ball123", 2300, 2, 2),
                arguments("camille123", 5100, 3, 2),
                arguments("daisy123", 4800, 4, 2),
                arguments("emily123", 7000, 2, 2)
        );
    }

    @ParameterizedTest
    @MethodSource("nameAndExpectedProvider")
    void 유저_결제수단_조회_테스트(String userName, int savings, int pointSize, int couponSize) {
        validate(userName, new Expected(savings, pointSize, couponSize));
    }

    @Test
    void 존재하지_않는_회원() {
        assertThrows(UserNotFoundException.class, () -> userPayInfoService.getPayInfo("maetdori"));
    }

    void validate(String userName, Expected expected) {
        UserPayInfoDto userInfo = userPayInfoService.getPayInfo(userName);

        SavingsDto savings = userInfo.getSavings();
        List<PointDto> points = userInfo.getPoints();
        List<CouponDto> coupons = userInfo.getCoupons();

        assertThat(savings.getAmount()).isEqualTo(expected.savings);
        assertThat(points.size()).isEqualTo(expected.pointSize);
        assertThat(coupons.size()).isEqualTo(expected.couponSize);
        for (PointDto point : points) {
            assertThat(point.getExpiryDate()).isAfterOrEqualTo(LocalDate.now());
        }
    }
}
