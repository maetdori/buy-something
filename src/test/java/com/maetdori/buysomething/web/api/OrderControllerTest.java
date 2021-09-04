package com.maetdori.buysomething.web.api;

import com.maetdori.buysomething.SampleDataInput;
import com.maetdori.buysomething.domain.Coupon.CouponRepository;
import com.maetdori.buysomething.domain.Point.PointRepository;
import com.maetdori.buysomething.domain.Savings.SavingsRepository;
import com.maetdori.buysomething.domain.User.UserRepository;
import com.maetdori.buysomething.exception.UserNotFoundException;
import com.maetdori.buysomething.web.dto.UserRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class OrderControllerTest {
	@Autowired
	OrderController orderController;
	@Autowired
	UserRepository userRepo;
	@Autowired
	SavingsRepository savingsRepo;
	@Autowired
	CouponRepository couponRepo;
	@Autowired
	PointRepository pointRepo;

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

	@Test
	void 존재하지_않는_회원() {
		UserRequest userRequest = new UserRequest("andy");

		assertThrows(UserNotFoundException.class, () -> orderController.getUserInfo(userRequest));
	}
}
