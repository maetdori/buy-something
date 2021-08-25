package com.maetdori.buysomething.service;

import com.maetdori.buysomething.exception.NoSuchUserException;
import com.maetdori.buysomething.web.dto.UserDto;

public interface AutoSelectService {
	UserDto.AutoSelect getSelection(UserDto.Request userRequest) throws NoSuchUserException;
}
