package com.maetdori.buysomething.domain.Coupon;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    List<Coupon> findAllByUserIdAndUsedFalse(Integer userId);
    Optional<Coupon> findByPaymentId(Integer paymentId);
}
