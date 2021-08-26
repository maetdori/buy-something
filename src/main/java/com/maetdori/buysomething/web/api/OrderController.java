package com.maetdori.buysomething.web.api;

import com.maetdori.buysomething.exception.NoSuchUserException;
import com.maetdori.buysomething.service.AutoSelectService;
import com.maetdori.buysomething.service.UserInfoService;
import com.maetdori.buysomething.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OrderController {
    private final UserInfoService userInfoService;
    private final AutoSelectService autoSelectService;

    @PostMapping("/order")
    public UserDto.Info getUserInfo(@RequestBody UserDto userRequest) throws NoSuchUserException {
        return userInfoService.getUserInfo(userRequest.getRequest());
    }

    @PostMapping("/order/auto-select")
    public UserDto.Selection getSelection(@RequestBody UserDto autoSelectRequest) {
        return autoSelectService.getSelection(autoSelectRequest.getInfo());
    }

}
