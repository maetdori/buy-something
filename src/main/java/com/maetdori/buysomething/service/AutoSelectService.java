package com.maetdori.buysomething.service;

import com.maetdori.buysomething.web.dto.UserDto;

public interface AutoSelectService {
	UserDto.Selection getSelection(UserDto.Info autoSelectRequest);
}
