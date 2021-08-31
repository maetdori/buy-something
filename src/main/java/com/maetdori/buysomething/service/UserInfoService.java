package com.maetdori.buysomething.service;

import com.maetdori.buysomething.exception.NoSuchUserException;
import com.maetdori.buysomething.web.dto.UserInfo;
import com.maetdori.buysomething.web.dto.UserRequest;

public interface UserInfoService {
    UserInfo getUserInfo(UserRequest userRequest) throws NoSuchUserException;
}
