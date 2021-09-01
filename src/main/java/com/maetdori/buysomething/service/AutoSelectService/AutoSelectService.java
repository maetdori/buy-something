package com.maetdori.buysomething.service.AutoSelectService;

import com.maetdori.buysomething.web.dto.Selection;
import com.maetdori.buysomething.web.dto.UserInfo;

public interface AutoSelectService {
	Selection getSelection(UserInfo autoSelectRequest);
}
