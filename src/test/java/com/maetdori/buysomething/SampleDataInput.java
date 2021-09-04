package com.maetdori.buysomething;

import com.maetdori.buysomething.domain.Coupon.Coupon;
import com.maetdori.buysomething.domain.Coupon.CouponRepository;
import com.maetdori.buysomething.domain.Coupon.CouponType;
import com.maetdori.buysomething.domain.Point.Point;
import com.maetdori.buysomething.domain.Point.PointRepository;
import com.maetdori.buysomething.domain.Savings.Savings;
import com.maetdori.buysomething.domain.Savings.SavingsRepository;
import com.maetdori.buysomething.domain.User.User;
import com.maetdori.buysomething.domain.User.UserRepository;

import java.time.LocalDate;

public class SampleDataInput {
	public SampleDataInput(UserRepository userRepo, SavingsRepository savingsRepo, CouponRepository couponRepo, PointRepository pointRepo) {
		insertUser1(userRepo, savingsRepo, couponRepo, pointRepo);
		insertUser2(userRepo, savingsRepo, couponRepo, pointRepo);
		insertUser3(userRepo, savingsRepo, couponRepo, pointRepo);
		insertUser4(userRepo, savingsRepo, couponRepo, pointRepo);
		insertUser5(userRepo, savingsRepo, couponRepo, pointRepo);
	}

	private void insertUser1(UserRepository userRepo, SavingsRepository savingsRepo, CouponRepository couponRepo, PointRepository pointRepo) {
		User user = userRepo.save(User.builder().userName("andy123").build());

		savingsRepo.save(Savings.builder().user(user).amount(1000).build());

		couponRepo.save(Coupon.builder().user(user).couponType(CouponType.SUMMER_EVENT).build());
		couponRepo.save(Coupon.builder().user(user).couponType(CouponType.NEWBIE_COUPON).build());

		pointRepo.save(Point.builder().user(user).amount(4800).expiryDate(LocalDate.of(2021,8,25)).build());
		pointRepo.save(Point.builder().user(user).amount(1200).expiryDate(LocalDate.of(2021,9,25)).build());
		pointRepo.save(Point.builder().user(user).amount(500).expiryDate(LocalDate.of(2021,12,12)).build());
		pointRepo.save(Point.builder().user(user).amount(3000).expiryDate(LocalDate.of(2021,8,14)).build());
		pointRepo.save(Point.builder().user(user).amount(1400).expiryDate(LocalDate.of(2021,9,30)).build());
	}

	private void insertUser2(UserRepository userRepo, SavingsRepository savingsRepo, CouponRepository couponRepo, PointRepository pointRepo) {
		User user = userRepo.save(User.builder().userName("ball123").build());

		savingsRepo.save(Savings.builder().user(user).amount(2300).build());

		couponRepo.save(Coupon.builder().user(user).couponType(CouponType.APP_DOWNLOAD_COUPON).build());
		couponRepo.save(Coupon.builder().user(user).couponType(CouponType.SUMMER_EVENT).build());

		pointRepo.save(Point.builder().user(user).amount(7000).expiryDate(LocalDate.of(2021,9,19)).build());
		pointRepo.save(Point.builder().user(user).amount(13200).expiryDate(LocalDate.of(2021,8,31)).build());
		pointRepo.save(Point.builder().user(user).amount(1900).expiryDate(LocalDate.of(2021,6,5)).build());
		pointRepo.save(Point.builder().user(user).amount(1300).expiryDate(LocalDate.of(2021,12,12)).build());
		pointRepo.save(Point.builder().user(user).amount(4200).expiryDate(LocalDate.of(2021,6,5)).build());
	}

	private void insertUser3(UserRepository userRepo, SavingsRepository savingsRepo, CouponRepository couponRepo, PointRepository pointRepo) {
		User user = userRepo.save(User.builder().userName("camille123").build());

		savingsRepo.save(Savings.builder().user(user).amount(5100).build());

		couponRepo.save(Coupon.builder().user(user).couponType(CouponType.NEWBIE_COUPON).build());
		couponRepo.save(Coupon.builder().user(user).couponType(CouponType.APP_DOWNLOAD_COUPON).build());

		pointRepo.save(Point.builder().user(user).amount(3000).expiryDate(LocalDate.of(2022,3,3)).build());
		pointRepo.save(Point.builder().user(user).amount(380).expiryDate(LocalDate.of(2021,8,4)).build());
		pointRepo.save(Point.builder().user(user).amount(8100).expiryDate(LocalDate.of(2021,7,20)).build());
		pointRepo.save(Point.builder().user(user).amount(200).expiryDate(LocalDate.of(2022,1,1)).build());
		pointRepo.save(Point.builder().user(user).amount(400).expiryDate(LocalDate.of(2021,9,14)).build());
	}

	private void insertUser4(UserRepository userRepo, SavingsRepository savingsRepo, CouponRepository couponRepo, PointRepository pointRepo) {
		User user = userRepo.save(User.builder().userName("daisy123").build());

		savingsRepo.save(Savings.builder().user(user).amount(4800).build());

		couponRepo.save(Coupon.builder().user(user).couponType(CouponType.SUMMER_EVENT).build());
		couponRepo.save(Coupon.builder().user(user).couponType(CouponType.NEWBIE_COUPON).build());

		pointRepo.save(Point.builder().user(user).amount(710).expiryDate(LocalDate.of(2022,3,8)).build());
		pointRepo.save(Point.builder().user(user).amount(12300).expiryDate(LocalDate.of(2021,8,21)).build());
		pointRepo.save(Point.builder().user(user).amount(4010).expiryDate(LocalDate.of(2021,10,10)).build());
		pointRepo.save(Point.builder().user(user).amount(480).expiryDate(LocalDate.of(2022,4,4)).build());
		pointRepo.save(Point.builder().user(user).amount(3600).expiryDate(LocalDate.of(2021,9,10)).build());
	}

	private void insertUser5(UserRepository userRepo, SavingsRepository savingsRepo, CouponRepository couponRepo, PointRepository pointRepo) {
		User user = userRepo.save(User.builder().userName("emily123").build());

		savingsRepo.save(Savings.builder().user(user).amount(7000).build());

		couponRepo.save(Coupon.builder().user(user).couponType(CouponType.APP_DOWNLOAD_COUPON).build());
		couponRepo.save(Coupon.builder().user(user).couponType(CouponType.SUMMER_EVENT).build());

		pointRepo.save(Point.builder().user(user).amount(6200).expiryDate(LocalDate.of(2024,12,24)).build());
		pointRepo.save(Point.builder().user(user).amount(1300).expiryDate(LocalDate.of(2021,12,25)).build());
		pointRepo.save(Point.builder().user(user).amount(380).expiryDate(LocalDate.of(2021,7,30)).build());
		pointRepo.save(Point.builder().user(user).amount(920).expiryDate(LocalDate.of(2021,8,14)).build());
		pointRepo.save(Point.builder().user(user).amount(14000).expiryDate(LocalDate.of(2021,8,30)).build());
	}
}

