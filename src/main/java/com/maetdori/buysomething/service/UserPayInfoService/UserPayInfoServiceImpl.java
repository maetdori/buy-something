package com.maetdori.buysomething.service.UserPayInfoService;

import com.maetdori.buysomething.domain.Coupon.CouponRepository;
import com.maetdori.buysomething.domain.Point.PointRepository;
import com.maetdori.buysomething.domain.Savings.SavingsRepository;
import com.maetdori.buysomething.domain.User.User;
import com.maetdori.buysomething.domain.User.UserRepository;
import com.maetdori.buysomething.exception.Business.EntityNotFound.UserNotFoundException;
import com.maetdori.buysomething.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserPayInfoServiceImpl implements UserPayInfoService {
    private final UserRepository userRepo;
    private final PointRepository pointRepo;
    private final CouponRepository couponRepo;
    private final SavingsRepository savingsRepo;

    private static final int POINT_MIN_AMOUNT = 0;

    @Override
    @Transactional(readOnly = true)
    public UserPayInfoDto getPayInfo(String userName) {
        //userName 에 해당하는 유저가 없을 경우 UserNotFoundException 던진다
        User user = userRepo.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException());

        return this.getPayInfo(user.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public UserPayInfoDto getPayInfo(Integer userId) {
        //유저가 가진 적립금
        SavingsDto savings = new SavingsDto(savingsRepo.findByUserId(userId));

        //유저가 가진 포인트들 중 (포인트액수 > 0 && 만료일 >= 현재)를 만족하는 포인트 리스트
        //없으면 empty 리스트 반환
        List<PointDto> points = pointRepo
                .findAllByUserIdAndAmountGreaterThanAndExpiryDateIsGreaterThanEqual(userId, POINT_MIN_AMOUNT, LocalDate.now())
                .stream().map(PointDto::new).collect(Collectors.toList());

        //유저가 가진 쿠폰들 중 아직 사용하지 않은 쿠폰 리스트
        //없으면 empty 리스트 반환
        List<CouponDto> coupons = couponRepo.findAllByUserIdAndUsedFalse(userId)
                .stream().map(CouponDto::new).collect(Collectors.toList());

        return UserPayInfoDto.builder()
                .userId(userId)
                .savings(savings)
                .points(points)
                .coupons(coupons)
                .build();
    }
}
