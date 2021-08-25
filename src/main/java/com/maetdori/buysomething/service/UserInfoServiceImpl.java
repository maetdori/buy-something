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

    @Override
    @Transactional
    public UserDto.Info getUserInfo(UserDto.Request userRequest) throws NoSuchUserException {
        Long userId = userRequest.getUserId();

        User user = userRepo.findById(userId).orElseThrow(()
                -> new NoSuchUserException(messageSource.getMessage("userNotFound", null, LocaleContextHolder.getLocale())));

        int savings = user.getSavings();

        List<PointDto> points = pointRepo.findAllByUserIdAndExpiryDateGreaterThanEqual(userId, LocalDate.now())
                .stream().map(PointDto::new).collect(Collectors.toList());

        List<CouponDto> coupons = couponRepo.findAllByUserIdAndMinAmountLessThan(userId, userRequest.getAmount())
                .stream().map(CouponDto::new).collect(Collectors.toList());

        return new UserDto.Info(userId, savings, points, coupons);
    }
}
