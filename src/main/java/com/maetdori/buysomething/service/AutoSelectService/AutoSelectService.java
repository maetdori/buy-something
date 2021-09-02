package com.maetdori.buysomething.service.AutoSelectService;

import com.maetdori.buysomething.web.dto.SelectionDto;
import com.maetdori.buysomething.web.dto.UserInfoDto;

public interface AutoSelectService {
	SelectionDto getSelection(UserInfoDto autoSelectRequest);
}
