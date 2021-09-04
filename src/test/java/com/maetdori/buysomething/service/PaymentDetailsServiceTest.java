package com.maetdori.buysomething.service;

import com.maetdori.buysomething.SampleDataInput;
import com.maetdori.buysomething.domain.Coupon.CouponRepository;
import com.maetdori.buysomething.domain.Payment.PaymentRepository;
import com.maetdori.buysomething.domain.Point.PointRepository;
import com.maetdori.buysomething.domain.PointUsed.PointUsedRepository;
import com.maetdori.buysomething.domain.Savings.SavingsRepository;
import com.maetdori.buysomething.domain.SavingsUsed.SavingsUsedRepository;
import com.maetdori.buysomething.domain.User.UserRepository;
import com.maetdori.buysomething.service.AutoSelectService.AutoSelectService;
import com.maetdori.buysomething.service.MakePaymentService.MakePaymentService;
import com.maetdori.buysomething.service.PaymentDetailsService.PaymentDetailsService;
import com.maetdori.buysomething.service.PaymentHistoryService.PaymentHistoryService;
import com.maetdori.buysomething.service.UserInfoService.UserInfoService;
import com.maetdori.buysomething.web.dto.PaymentDto;
import com.maetdori.buysomething.web.dto.SelectionDto;
import com.maetdori.buysomething.web.dto.UserRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class PaymentDetailsServiceTest {
	@Autowired UserInfoService userInfoService;
	@Autowired AutoSelectService autoSelectService;
	@Autowired MakePaymentService makePaymentService;
	@Autowired PaymentHistoryService paymentHistoryService;
	@Autowired PaymentDetailsService paymentDetailsService;
	@Autowired UserRepository userRepo;
	@Autowired PaymentRepository paymentRepo;
	@Autowired SavingsRepository savingsRepo;
	@Autowired CouponRepository couponRepo;
	@Autowired PointRepository pointRepo;
	@Autowired SavingsUsedRepository savingsUsedRepo;
	@Autowired PointUsedRepository pointUsedRepo;

	@BeforeAll
	void setup() {
		new SampleDataInput(userRepo, savingsRepo, couponRepo, pointRepo);
	}

	@AfterAll
	void clear() {
		savingsUsedRepo.deleteAll();
		pointUsedRepo.deleteAll();
		savingsRepo.deleteAll();
		pointRepo.deleteAll();
		couponRepo.deleteAll();
		paymentRepo.deleteAll();
		userRepo.deleteAll();
	}

	//사용자, 주문금액
	Stream<Arguments> paymentHistoryTester() {
		return Stream.of(
				arguments(new UserRequest("andy123"), 56000),
				arguments(new UserRequest("ball123"), 78000),
				arguments(new UserRequest("camille123"), 28000),
				arguments(new UserRequest("daisy123"), 150000),
				arguments(new UserRequest("emily123"), 12000)
		);
	}

	@ParameterizedTest
	@MethodSource("paymentHistoryTester")
	void 결제이력_조회_테스트(UserRequest user, int cartAmount) {
		Integer userId = userRepo.findByUserName(user.getUserName()).get().getId();

		SelectionDto selection = autoSelectService.getSelection(userInfoService.getUserInfo(user));
		selection.setCartAmount(cartAmount);
		Integer paymentId = makePaymentService.makePayment(selection, LocalDateTime.now());
		assertThat(paymentHistoryService.getPaymentList(userId).size()).isEqualTo(1);

		결제내역_상세_테스트(selection, paymentId);

		selection = autoSelectService.getSelection(userInfoService.getUserInfo(user));
		selection.setCartAmount(cartAmount);
		paymentId = makePaymentService.makePayment(selection, LocalDateTime.now());
		assertThat(paymentHistoryService.getPaymentList(userId).size()).isEqualTo(2);

		결제내역_상세_테스트(selection, paymentId);
	}

	void 결제내역_상세_테스트(SelectionDto selection, Integer paymentId) {
		PaymentDto payment = paymentDetailsService.getPaymentDetails(selection.getUserId(), paymentId);

		assertThat(payment.getCartAmount()).isEqualTo(selection.getCartAmount());
		assertThat(payment.getUsedCoupon()).isEqualTo(selection.getCouponToUse());
		assertThat(payment.getUsedPoints()).isEqualTo(selection.getPointsToUse());
		assertThat(payment.getUsedSavings()).isEqualTo(selection.getSavingsToUse());
	}
}
