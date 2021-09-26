package com.example.coupon.service;

import com.example.coupon.entity.Coupon;
import com.example.coupon.entity.CouponMeta;
import com.example.coupon.entity.CouponStatus;
import com.example.coupon.entity.User;
import com.example.coupon.repository.CouponRepository;
import com.example.coupon.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@Transactional
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponMetaService couponMetaService;
    private final UserService userService;

    @Autowired
    public CouponService(
            CouponRepository couponRepository,
            CouponMetaService couponMetaService,
            UserService userService) {
        this.couponMetaService = couponMetaService;
        this.couponRepository = couponRepository;
        this.userService = userService;
    }

    public List<Coupon> getList() {
        return couponRepository.findAll();
    }

//    public Coupon add(Coupon coupon) {
//        Optional<Coupon> old = findBySerialNumber(coupon.getSerialNumber());
//        if (old.isPresent()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "duplicate serialNumber: " + coupon.getSerialNumber());
//        }
//
//        if (coupon.getExpiredAt() == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "expiredAt must set");
//        }
//
//        coupon.setStatus(CouponStatus.NONE);
//        coupon.setCreatedAt(LocalDateTime.now());
//        coupon.setUpdatedAt(LocalDateTime.now());
//        return couponRepository.save(coupon);
//    }

    public Optional<Coupon> findBySerialNumber(String code) {
        return couponRepository.findBySerialNumber(code);
    }

//    public Coupon update(String serialNumber, Coupon coupon) {
//        Coupon old = getCoupon(serialNumber);
//
//        if (coupon.getExpiredAt() == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "expiredAt or duration must set");
//        }
//
//        old.setUserId(coupon.getUserId());
//        old.setStatus(coupon.getStatus());
//        old.setUpdatedAt(LocalDateTime.now());
//        return couponRepository.save(old);
//    }

    public List<String> makeCoupon(String couponCode, int count) {
        Optional<CouponMeta> optionalCouponMeta = couponMetaService.findByCode(couponCode);
        if (optionalCouponMeta.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "not exist coupon:" + couponCode);
        }

        CouponMeta meta = optionalCouponMeta.get();
        LocalDateTime expiredAt = meta.getExpiredAt();
        if (expiredAt == null) {
            expiredAt = LocalDateTime.now().plusDays(meta.getDuration()+1).with(LocalTime.MIN);
        }

        List<Coupon> couponList = new ArrayList<>();
        for(int i=0; i<count; ++i) {
            Coupon coupon = new Coupon();
            coupon.setCouponCode(couponCode);
            coupon.setSerialNumber(issueSerialNumber());
            coupon.setStatus(CouponStatus.NONE);
            coupon.setCreatedAt(LocalDateTime.now());
            coupon.setExpiredAt(expiredAt);
            couponRepository.save(coupon);
            couponList.add(coupon);
        }

        return Arrays.asList(
                couponList
                        .stream()
                        .map((x) -> x.getSerialNumber())
                        .toArray(String[]::new));
    }

    public Coupon assignCoupon(String serialNumber, long userId) {
        Coupon coupon = getCoupon(serialNumber);
        if (coupon.getUserId() != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "already assigned serialNumber");
        }

        checkUsableCoupon(coupon);

        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid user");
        }

        coupon.setUserId(userId);
        return couponRepository.save(coupon);
    }

    public List<Coupon> findCouponByUserId(long userId) {
        return couponRepository.findByUserid(userId);
    }

    public Coupon useCoupon(String serialNumber) {
        Coupon coupon = getCoupon(serialNumber);
        if (coupon.getUserId() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "not assigned coupon");
        }
        checkUsableCoupon(coupon);

        // TODO for reward
        Optional<CouponMeta> optionalMeta = couponMetaService.findByCode(coupon.getCouponCode());
        if (optionalMeta.isEmpty()) {
           throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "not found couponCode: " + coupon.getCouponCode());
        }

        coupon.setStatus(CouponStatus.USED);
        return couponRepository.save(coupon);
    }

    public Coupon useCancelCoupon(String serialNumber) {
        Coupon coupon = getCoupon(serialNumber);
        if (coupon.getUserId() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "not assigned coupon");
        }

        if (coupon.getStatus() != CouponStatus.USED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "coupon is not used");
        }

        if (coupon.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "expired coupon");
        }

        // TODO for retrieve reward
        Optional<CouponMeta> optionalMeta = couponMetaService.findByCode(coupon.getCouponCode());
        if (optionalMeta.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "not found couponCode: " + coupon.getCouponCode());
        }

        coupon.setStatus(CouponStatus.NONE);
        return couponRepository.save(coupon);
    }

    public List<Coupon> getExpiredTodayCoupon() {
        return couponRepository.findAllExpiredToday();
    }

    /**
     * not found: throw ResponseStatusException()
     * @param serialNumber
     * @return Coupon
     */
    private Coupon getCoupon(String serialNumber) {
        Optional<Coupon> optionalCoupon = couponRepository.findBySerialNumber(serialNumber);
        if (optionalCoupon.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "not exist coupon:" + serialNumber);
        }
        return optionalCoupon.get();
    }

    /**
     * check: status, expired_at
     * error: throw ResponseStatusException()
     */
    private void checkUsableCoupon(Coupon coupon) {
        if (coupon.getStatus() != CouponStatus.NONE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid coupon");
        }
        if (coupon.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "expired coupon");
        }
    }

    private String issueSerialNumber() {
        return UUID.randomUUID().toString();
    }
}
