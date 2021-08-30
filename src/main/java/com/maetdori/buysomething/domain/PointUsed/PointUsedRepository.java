package com.maetdori.buysomething.domain.PointUsed;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PointUsedRepository extends JpaRepository<PointUsed, Long> {
    Long countAllByUserId(Long userId);
    PointUsed findByPointIdAndPaymentId(Long pointId, Long paymentId);
}