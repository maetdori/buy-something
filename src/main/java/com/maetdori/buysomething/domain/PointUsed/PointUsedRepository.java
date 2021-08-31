package com.maetdori.buysomething.domain.PointUsed;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PointUsedRepository extends JpaRepository<PointUsed, Integer> {
    PointUsed findByPointIdAndPaymentId(Integer pointId, Integer paymentId);
}