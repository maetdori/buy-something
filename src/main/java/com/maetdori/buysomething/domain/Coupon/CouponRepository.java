package com.maetdori.buysomething.domain.Coupon;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findAllByUserId(Long userId);
}
