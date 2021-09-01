package com.maetdori.buysomething.service;

import com.maetdori.buysomething.exception.NoSuchUserException;
import com.maetdori.buysomething.service.AutoSelectService.AutoSelectService;
import com.maetdori.buysomething.service.UserInfoService.UserInfoService;
import com.maetdori.buysomething.validation.UserValidation;
import com.maetdori.buysomething.web.dto.Selection;
import com.maetdori.buysomething.web.dto.UserInfo;
import com.maetdori.buysomething.web.dto.UserRequest;
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

	UserValidation userValidation;

	//사용자이름, 주문금액, 자동할인 적용 후 금액
	static Stream<Arguments> nameAndExpectedCostProvider() {
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
	public void 결제수단_자동선택_테스트(String userName, int cartAmount, int expectedCost) throws NoSuchUserException {
		UserInfo userInfo = userInfoService.getUserInfo(userValidation.getUserIfExist(new UserRequest(userName)).getId());
		userInfo.setCartAmount(cartAmount);
		Selection selection = autoSelectService.getSelection(userInfo);
		assertThat(selection.getPayAmount()).isEqualTo(expectedCost);
	}
}
