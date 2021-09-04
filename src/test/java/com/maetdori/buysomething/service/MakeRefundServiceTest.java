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
import com.maetdori.buysomething.service.MakeRefundService.MakeRefundService;
import com.maetdori.buysomething.service.PaymentDetailsService.PaymentDetailsService;
import com.maetdori.buysomething.service.UserPayInfoService.UserPayInfoService;
import com.maetdori.buysomething.web.dto.RefundDto;
import com.maetdori.buysomething.web.dto.SelectionDto;
import com.maetdori.buysomething.web.dto.UserPayInfoDto;
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
public class MakeRefundServiceTest {
	@Autowired UserPayInfoService userPayInfoService;
	@Autowired AutoSelectService autoSelectService;
	@Autowired MakePaymentService makePaymentService;
	@Autowired PaymentDetailsService paymentDetailsService;
	@Autowired MakeRefundService makeRefundService;
	@Autowired UserRepository userRepo;
	@Autowired SavingsRepository savingsRepo;
	@Autowired CouponRepository couponRepo;
	@Autowired PointRepository pointRepo;
	@Autowired PaymentRepository paymentRepo;
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

	//사용자, 주문금액, 결제금액, 환불금액
	Stream<Arguments> refundTester() {
		return Stream.of(
				arguments("andy123", 56000, 43500, 40400),
				arguments("ball123", 78000, 55700, 47400),
				arguments("camille123", 28000, 16500, 12900),
				arguments("daisy123", 150000, 113900, 105100),
				arguments("emily123", 12000, 0, 0)
		);
	}

	@ParameterizedTest
	@MethodSource("refundTester")
	void 환불요청_처리_테스트(String userName, int cartAmount, int payAmount, int refundAmount) {
		Integer userId = userRepo.findByUserName(userName).get().getId();

		UserPayInfoDto userPayInfo = userPayInfoService.getPayInfo(userName);
		SelectionDto selection = autoSelectService.getSelection(userPayInfo, cartAmount);

		Integer paymentId = makePaymentService.makePayment(selection, LocalDateTime.now());

		RefundDto refund = makeRefundService.makeRefund(userId, paymentId, LocalDateTime.of(2022,6,30,12,10));

		assertThat(refund.getRepayAmount()).isLessThanOrEqualTo(payAmount);
		assertThat(refund.getRepayAmount()).isEqualTo(refundAmount);
	}
}
