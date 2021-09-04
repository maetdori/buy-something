package com.maetdori.buysomething.service;

import com.maetdori.buysomething.SampleDataInput;
import com.maetdori.buysomething.domain.Coupon.CouponRepository;
import com.maetdori.buysomething.domain.Point.PointRepository;
import com.maetdori.buysomething.domain.Savings.SavingsRepository;
import com.maetdori.buysomething.domain.User.UserRepository;
import com.maetdori.buysomething.exception.Business.InvalidValue.ZeroCartAmountException;
import com.maetdori.buysomething.service.AutoSelectService.AutoSelectService;
import com.maetdori.buysomething.service.UserPayInfoService.UserPayInfoService;
import com.maetdori.buysomething.web.dto.SelectionDto;
import com.maetdori.buysomething.web.dto.UserPayInfoDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class AutoSelectServiceTest {
	@Autowired UserPayInfoService userPayInfoService;
	@Autowired AutoSelectService autoSelectService;
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

	//사용자이름, 주문금액, 자동할인 적용 후 금액
	Stream<Arguments> nameAndExpectedCostProvider() {
		return Stream.of(
				arguments("andy123", 56000, 43500),
				arguments("ball123", 78000, 55700),
				arguments("camille123", 28000, 16500),
				arguments("daisy123", 150000, 113900),
				arguments("emily123", 12000, 0)
		);
	}

	@ParameterizedTest
	@MethodSource("nameAndExpectedCostProvider")
	void 결제수단_자동선택_테스트(String userName, int cartAmount, int expectedCost) {
		UserPayInfoDto userPayInfo = userPayInfoService.getPayInfo(userName);
		SelectionDto selection = autoSelectService.getSelection(userPayInfo, cartAmount);
		assertThat(selection.getPayAmount()).isEqualTo(expectedCost);
	}

	@Test
	void 주문금액_입력값이_0인_경우() {
		UserPayInfoDto userPayInfo = userPayInfoService.getPayInfo("andy123");
		assertThrows(ZeroCartAmountException.class, () -> autoSelectService.getSelection(userPayInfo, 0));
	}
}
