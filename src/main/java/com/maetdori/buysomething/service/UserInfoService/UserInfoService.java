package com.maetdori.buysomething.service.UserInfoService;

import com.maetdori.buysomething.exception.NoSuchUserException;
import com.maetdori.buysomething.web.dto.UserInfo;

public interface UserInfoService {
    UserInfo getUserInfo(Integer userId) throws NoSuchUserException;
}
