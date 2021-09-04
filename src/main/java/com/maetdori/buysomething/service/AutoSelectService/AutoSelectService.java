package com.maetdori.buysomething.service.AutoSelectService;

import com.maetdori.buysomething.web.dto.SelectionDto;
import com.maetdori.buysomething.web.dto.UserPayInfoDto;

public interface AutoSelectService {
	SelectionDto getSelection(UserPayInfoDto autoSelectRequest, int cartAmount);
}
