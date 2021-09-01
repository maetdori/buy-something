package com.maetdori.buysomething.domain.Payment;

import com.maetdori.buysomething.domain.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findAllByUserIdOrderByPurchaseDateDesc(Integer userId);
}