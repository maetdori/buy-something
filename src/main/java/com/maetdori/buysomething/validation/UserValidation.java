package com.maetdori.buysomething.validation;

import com.maetdori.buysomething.domain.User.User;
import com.maetdori.buysomething.domain.User.UserRepository;
import com.maetdori.buysomething.exception.NoSuchUserException;
import com.maetdori.buysomething.web.dto.UserRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

@RequiredArgsConstructor
public class UserValidation {
    private final UserRepository userRepo;

    private final MessageSource messageSource;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public User getUserIfExist(UserRequest userRequest) {
        return userRepo.findByUserName(userRequest.getUserName()).orElseThrow(() -> {
            String message = messageSource.getMessage("userNotFound.msg", null, LocaleContextHolder.getLocale());
            logger.error(message);
            return new NoSuchUserException(message);
        });
    }
}
