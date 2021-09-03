package com.maetdori.buysomething.validator.User;

import com.maetdori.buysomething.domain.User.UserRepository;
import com.maetdori.buysomething.exception.NoSuchUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

@RequiredArgsConstructor
public class UserValidator {
	private static MessageSource messageSource;

	public static void doesUserNameExist(String userName, UserRepository userRepo) {
		userRepo.findByUserName(userName).orElseThrow(() -> {
			String message = messageSource.getMessage("userNotFound.msg", null, LocaleContextHolder.getLocale());
			return new NoSuchUserException(message);
		});
	}
}
