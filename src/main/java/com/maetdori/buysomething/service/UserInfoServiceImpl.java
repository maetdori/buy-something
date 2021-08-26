package com.maetdori.buysomething.service;

import com.maetdori.buysomething.domain.Coupon.CouponRepository;
import com.maetdori.buysomething.domain.Point.PointRepository;
import com.maetdori.buysomething.domain.User.User;
import com.maetdori.buysomething.domain.User.UserRepository;
import com.maetdori.buysomething.exception.NoSuchUserException;
import com.maetdori.buysomething.web.dto.CouponDto;
import com.maetdori.buysomething.web.dto.PointDto;
import com.maetdori.buysomething.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserInfoServiceImpl implements UserInfoService {
    private final UserRepository userRepo;
    private final PointRepository pointRepo;
    private final CouponRepository couponRepo;

    @Autowired
    private MessageSource messageSource;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    @Transactional
    public UserDto.Info getUserInfo(UserDto.Request userRequest) throws NoSuchUserException {
        User user = userRepo.findByUserName(userRequest.getUserName()).orElseThrow(() -> {
            String message = messageSource.getMessage("userNotFound.msg", null, LocaleContextHolder.getLocale());
            logger.error(message);
            return new NoSuchUserException(message);
        });

        Long userId = user.getId();

        int savings = user.getSavings();

        List<PointDto> points = pointRepo.findAllByUserIdAndExpiryDateGreaterThanEqual(userId, LocalDate.now())
                .stream().map(PointDto::new).collect(Collectors.toList());

        List<CouponDto> coupons = couponRepo.findAllByUserId(userId)
                .stream().map(CouponDto::new).collect(Collectors.toList());

        return new UserDto.Info(userId, 0, savings, points, coupons);
    }
}
