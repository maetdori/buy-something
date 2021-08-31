package com.maetdori.buysomething.service;

import com.maetdori.buysomething.domain.Coupon.CouponRepository;
import com.maetdori.buysomething.domain.Point.PointRepository;
import com.maetdori.buysomething.domain.Savings.SavingsRepository;
import com.maetdori.buysomething.domain.User.User;
import com.maetdori.buysomething.domain.User.UserRepository;
import com.maetdori.buysomething.exception.NoSuchUserException;
import com.maetdori.buysomething.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final SavingsRepository savingsRepo;

    private final MessageSource messageSource;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final int MIN_POINT = 0;
    private final int NULL_PAYMENT_ID = 0;

    @Override
    @Transactional
    public UserInfo getUserInfo(UserRequest userRequest) throws NoSuchUserException {
        User user = userRepo.findByUserName(userRequest.getUserName()).orElseThrow(() -> {
            String message = messageSource.getMessage("userNotFound.msg", null, LocaleContextHolder.getLocale());
            logger.error(message);
            return new NoSuchUserException(message);
        });

        Integer userId = user.getId();

        SavingsDto savings = new SavingsDto(savingsRepo.findByUserId(userId));

        //유저가 가진 포인트들 중 (포인트액수 > 0 && 만료일 >= 현재)를 만족하는 포인트 리스트
        //없으면 빈 리스트 반환
        List<PointDto> points = pointRepo.findAllByUserIdAndAmountGreaterThanAndExpiryDateIsGreaterThanEqual(userId, MIN_POINT, LocalDate.now())
                .stream().map(PointDto::new).collect(Collectors.toList());

        //유저가 가진 쿠폰들 중 아직 사용하지 않은 쿠폰 리스트
        //없으면 빈 리스트 반환
        List<CouponDto> coupons = couponRepo.findAllByUserIdAndExpiredFalse(userId)
                .stream().map(CouponDto::new).collect(Collectors.toList());

        return UserInfo.builder()
                .userId(userId)
                .savings(savings)
                .points(points)
                .coupons(coupons)
                .build();
    }
}
