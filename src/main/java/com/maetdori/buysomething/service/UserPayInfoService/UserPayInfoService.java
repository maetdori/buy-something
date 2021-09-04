package com.maetdori.buysomething.service.UserPayInfoService;

import com.maetdori.buysomething.web.dto.UserPayInfoDto;

public interface UserPayInfoService {
    UserPayInfoDto getPayInfo(Integer userId);
    UserPayInfoDto getPayInfo(String userName);
}
