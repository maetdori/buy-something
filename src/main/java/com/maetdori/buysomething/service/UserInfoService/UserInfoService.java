package com.maetdori.buysomething.service.UserInfoService;

import com.maetdori.buysomething.web.dto.UserInfoDto;
import com.maetdori.buysomething.web.dto.UserRequest;

public interface UserInfoService {
    UserInfoDto getUserInfo(UserRequest userRequest);
}
