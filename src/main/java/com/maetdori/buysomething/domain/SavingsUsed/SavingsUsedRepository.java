package com.maetdori.buysomething.domain.SavingsUsed;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SavingsUsedRepository extends JpaRepository<SavingsUsed, Integer> {
    Optional<SavingsUsed> findByPaymentId(Integer paymentId);
}
