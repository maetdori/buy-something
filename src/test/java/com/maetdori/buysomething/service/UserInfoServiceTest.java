package com.maetdori.buysomething.service;

import com.maetdori.buysomething.exception.NoSuchUserException;
import com.maetdori.buysomething.web.dto.CouponDto;
import com.maetdori.buysomething.web.dto.PointDto;
import com.maetdori.buysomething.web.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

@SpringBootTest
public class UserInfoServiceTest {
    @Autowired
    UserInfoService userInfoService;

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

    static Stream<Arguments> nameAndExpectedProvider() {
        return Stream.of(
                arguments("andy123", 1000, 3, 5),
                arguments("ball123", 2300, 3, 5),
                arguments("camille123", 5100, 3, 5),
                arguments("daisy123", 4800, 4, 5),
                arguments("emily123", 7000, 3, 5)
        );
    }

    @ParameterizedTest
    @MethodSource("nameAndExpectedProvider")
    public void 유저_결제수단_조회_테스트(String userName, int savings, int pointSize, int couponSize) throws NoSuchUserException {
        validate(new UserDto.Request(userName), new Expected(savings, pointSize, couponSize));
    }

    @Test
    @DisplayName("TEST: 존재하지 않는 회원")
    public void 존재하지_않는_회원() {
        UserDto.Request userRequest = new UserDto.Request("gibson");
        assertThrows(NoSuchUserException.class, () -> userInfoService.getUserInfo(userRequest));
    }

    public void validate(UserDto.Request userRequest, Expected expected) throws NoSuchUserException {
        UserDto.Info user = userInfoService.getUserInfo(userRequest);

        int savings = user.getSavings();
        List<PointDto> points = user.getPoints();
        List<CouponDto> coupons = user.getCoupons();

        assertThat(savings).isEqualTo(expected.savings);
        assertThat(points.size()).isEqualTo(expected.pointSize);
        for (PointDto point : points) {
            assertThat(point.getExpiryDate()).isAfterOrEqualTo(LocalDate.now());
        }
        assertThat(coupons.size()).isEqualTo(expected.couponSize);
    }
}
