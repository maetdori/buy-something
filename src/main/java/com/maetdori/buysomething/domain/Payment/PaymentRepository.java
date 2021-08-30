package com.maetdori.buysomething.domain.Payment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByUserIdOrderByPurchaseDateDesc(Long userId);
}
