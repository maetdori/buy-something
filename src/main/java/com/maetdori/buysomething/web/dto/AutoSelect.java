package com.maetdori.buysomething.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AutoSelect {
	private int savings;
	private Long couponId;
	private List<Point.Selected> points;
}
