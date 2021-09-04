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
import com.maetdori.buysomething.service.UserPayInfoService.UserPayInfoService;
import com.maetdori.buysomething.web.dto.PaymentDetailDto;
import com.maetdori.buysomething.web.dto.SelectionDto;
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
	@Autowired UserPayInfoService userPayInfoService;
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
				arguments("andy123", 56000),
				arguments("ball123", 78000),
				arguments("camille123", 28000),
				arguments("daisy123", 150000),
				arguments("emily123", 12000)
		);
	}

	@ParameterizedTest
	@MethodSource("paymentHistoryTester")
	void 결제이력_조회_테스트(String userName, int cartAmount) {
		Integer userId = userRepo.findByUserName(userName).get().getId();

		SelectionDto selection = autoSelectService.getSelection(userPayInfoService.getPayInfo(userName), cartAmount);
		selection.setCartAmount(cartAmount);
		Integer paymentId = makePaymentService.makePayment(selection, LocalDateTime.now());
		assertThat(paymentHistoryService.getPaymentList(userId).size()).isEqualTo(1);

		결제내역_상세_테스트(selection, paymentId);

		selection = autoSelectService.getSelection(userPayInfoService.getPayInfo(userName), cartAmount);
		selection.setCartAmount(cartAmount);
		paymentId = makePaymentService.makePayment(selection, LocalDateTime.now());
		assertThat(paymentHistoryService.getPaymentList(userId).size()).isEqualTo(2);

		결제내역_상세_테스트(selection, paymentId);
	}

	void 결제내역_상세_테스트(SelectionDto selection, Integer paymentId) {
		PaymentDetailDto payment = paymentDetailsService.getPaymentDetails(selection.getUserId(), paymentId);

		assertThat(payment.getCartAmount()).isEqualTo(selection.getCartAmount());
		if(payment.getUsedCoupon() != null)
			assertThat(payment.getUsedCoupon().getId()).isEqualTo(selection.getCouponToUse().getId());
		assertThat(payment.getUsedPoints().size()).isEqualTo(selection.getPointsToUse().size());
		assertThat(payment.getUsedSavings().getAmount()).isEqualTo(selection.getSavingsToUse().getAmount());
	}
}
