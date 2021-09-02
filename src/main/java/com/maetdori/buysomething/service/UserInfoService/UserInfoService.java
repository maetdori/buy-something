package com.maetdori.buysomething.service.UserInfoService;

import com.maetdori.buysomething.exception.NoSuchUserException;
import com.maetdori.buysomething.web.dto.UserInfoDto;

public interface UserInfoService {
    UserInfoDto getUserInfo(Integer userId) throws NoSuchUserException;
}
