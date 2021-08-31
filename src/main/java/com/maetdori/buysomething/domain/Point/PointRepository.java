package com.maetdori.buysomething.domain.Point;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PointRepository extends JpaRepository<Point, Integer> {
    List<Point> findAllByUserIdAndAmountGreaterThanAndExpiryDateIsGreaterThanEqual(Integer userId, int min, LocalDate now);
}
