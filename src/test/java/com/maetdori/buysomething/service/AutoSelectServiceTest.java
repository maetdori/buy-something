package com.maetdori.buysomething.service;

import com.maetdori.buysomething.exception.NoSuchUserException;
import com.maetdori.buysomething.web.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AutoSelectServiceTest {
	@Autowired
	UserInfoService userInfoService;
	@Autowired
	AutoSelectService autoSelectService;

	class TestVal {
		UserDto.Request userRequest;
		int expectedCost;

		TestVal(UserDto.Request userRequest, int expectedCost) {
			this.userRequest = userRequest;
			this.expectedCost = expectedCost;
		}
	}

	@Test
	public void 결제수단_자동선택_테스트() throws NoSuchUserException {
		List<TestVal> tests = new ArrayList<>();

		tests.add(new TestVal(new UserDto.Request(1L,56000), 40700));
		tests.add(new TestVal(new UserDto.Request(2L,78000), 42500));
		tests.add(new TestVal(new UserDto.Request(3L,28000), 16500));
		tests.add(new TestVal(new UserDto.Request(4L,150000), 61400));
		tests.add(new TestVal(new UserDto.Request(5L,12000), 0));

		for(TestVal testVal: tests)
			validate(testVal.userRequest, testVal.expectedCost);
	}

	public void validate(UserDto.Request user, int expectedCost) throws NoSuchUserException {
		UserDto.AutoSelect selection = autoSelectService.getSelection(user);
		assertThat(selection.getPayAmount()).isEqualTo(expectedCost);
	}
}
