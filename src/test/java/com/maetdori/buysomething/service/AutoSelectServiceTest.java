package com.maetdori.buysomething.service;

import com.maetdori.buysomething.exception.NoSuchUserException;
import com.maetdori.buysomething.web.dto.UserDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
public class AutoSelectServiceTest {
	@Autowired
	UserInfoService userInfoService;
	@Autowired
	AutoSelectService autoSelectService;

	static Stream<Arguments> nameAndExpectedCostProvider() {
		return Stream.of(
				arguments("andy123", 56000, 40700),
				arguments("ball123", 78000, 42500),
				arguments("camille123", 28000, 16500),
				arguments("daisy123", 150000, 61400),
				arguments("emily123", 12000, 0)
		);
	}

	@ParameterizedTest
	@MethodSource("nameAndExpectedCostProvider")
	public void 결제수단_자동선택_테스트(String userName, int cartAmount, int expectedCost) throws NoSuchUserException {
		UserDto.Info userInfo = userInfoService.getUserInfo(new UserDto.Request(userName));
		userInfo.setCartAmount(cartAmount);
		UserDto.Selection selection = autoSelectService.getSelection(userInfo);
		assertThat(selection.getPayAmount()).isEqualTo(expectedCost);
	}
}
