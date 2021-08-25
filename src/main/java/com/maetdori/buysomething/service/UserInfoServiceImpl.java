package com.maetdori.buysomething.service;

import com.maetdori.buysomething.domain.Coupon.CouponRepository;
import com.maetdori.buysomething.domain.Point.PointRepository;
import com.maetdori.buysomething.domain.User.User;
import com.maetdori.buysomething.domain.User.UserRepository;
import com.maetdori.buysomething.web.dto.CouponDto;
import com.maetdori.buysomething.web.dto.PointDto;
import com.maetdori.buysomething.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserInfoServiceImpl implements UserInfoService {
    private final UserRepository userRepo;
    private final PointRepository pointRepo;
    private final CouponRepository couponRepo;

    @Override
    @Transactional
    public UserDto.Info getUserInfo(UserDto.Request userReq) {
        Long userId = userReq.getUserId();
        User user = userRepo.findById(userId).orElseThrow(()
                -> new NoSuchElementException("존재하지 않는 회원입니다."));

        int savings = user.getSavings();

        List<PointDto> points = pointRepo.findAllByUserIdAndExpiryDateGreaterThanEqual(userId, LocalDate.now())
                .stream().map(PointDto::new).collect(Collectors.toList());

        List<CouponDto> coupons = couponRepo.findAllByUserIdAndMinAmountLessThan(userId, userReq.getAmount())
                .stream().map(CouponDto::new).collect(Collectors.toList());

        return new UserDto.Info(userId, savings, points, coupons);
    }
}
