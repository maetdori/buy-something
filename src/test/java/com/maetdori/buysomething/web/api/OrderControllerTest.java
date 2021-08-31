package com.maetdori.buysomething.web.api;

import com.maetdori.buysomething.exception.NoSuchUserException;
import com.maetdori.buysomething.web.dto.UserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class OrderControllerTest {
	@Autowired
	OrderController orderController;

	@Test
	@DisplayName("Test: 존재하지 않는 회원")
	public void 존재하지_않는_회원() {
		UserRequest userRequest = new UserRequest("andy");

		NoSuchUserException exception = assertThrows(NoSuchUserException.class, () ->
				orderController.getUserInfo(userRequest));

		String message = exception.getMessage();
		assertThat(message).isEqualTo("존재하지 않는 회원입니다.");
	}
}
