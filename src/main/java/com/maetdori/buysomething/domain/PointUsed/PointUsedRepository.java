package com.maetdori.buysomething.domain.PointUsed;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointUsedRepository extends JpaRepository<PointUsed, Integer> {
    List<PointUsed> findAllByPaymentId(Integer paymentId);
}