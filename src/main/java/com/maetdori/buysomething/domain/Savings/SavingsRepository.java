package com.maetdori.buysomething.domain.Savings;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingsRepository extends JpaRepository<Savings, Integer> {
	Savings findByUserId(Integer userId);
}
