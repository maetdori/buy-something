package com.maetdori.buysomething.domain.Enum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CouponTypeInverter implements AttributeConverter<CouponType, Integer> {

	@Override
	public Integer convertToDatabaseColumn(CouponType attribute) {
		return attribute.getCode();
	}

	@Override
	public CouponType convertToEntityAttribute(Integer dbData) {
		return CouponType.ofCode(dbData);
	}
}
