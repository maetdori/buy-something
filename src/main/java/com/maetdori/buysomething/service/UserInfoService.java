package com.maetdori.buysomething.service;

import com.maetdori.buysomething.web.dto.UserDto;

public interface UserInfoService {
    UserDto.Info getUserInfo(UserDto.Request userReq);
}
