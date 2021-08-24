package com.maetdori.buysomething.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Coupon {
	private Long id;
	private String name;
	private int discountRate;
	private int minAmount;
}
