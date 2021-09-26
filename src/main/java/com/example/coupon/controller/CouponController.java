package com.example.coupon.controller;

import com.example.coupon.controller.dto.AssignCouponDto;
import com.example.coupon.controller.dto.MakeCouponDto;
import com.example.coupon.entity.Coupon;
import com.example.coupon.entity.CouponMeta;
import com.example.coupon.service.CouponMetaService;
import com.example.coupon.service.CouponService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class CouponController {
    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping("/coupon")
    public List<Coupon> getList() {
        return couponService.getList();
    }

//    @PostMapping("/coupon")
//    public Coupon add(@RequestBody Coupon coupon) {
//        return couponService.add(coupon);
//    }

//    @PutMapping("/coupon/{serial-number}")
//    public Coupon update(@PathVariable("serial-number") String serialNumber, @RequestBody Coupon coupon) {
//        return couponService.update(serialNumber, coupon);
//    }

    @PostMapping("/coupon/make")
    public List<String> makeCoupon(@RequestBody MakeCouponDto dto) {
        if(dto.getCouponCode().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "input couponCode");
        }
        if (dto.getCount() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "count is greater then 0");
        }

        return couponService.makeCoupon(dto.getCouponCode(), dto.getCount());
    }

    @PostMapping("/coupon/assign")
    public Coupon assignCoupon(@RequestBody AssignCouponDto dto) {
        if(dto.getSerialNumber().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "input serialNumber");
        }
        if (dto.getUserId() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid userId");
        }

        return couponService.assignCoupon(dto.getSerialNumber(), dto.getUserId());
    }

    @GetMapping("/coupon/find-by-user/{user-id}")
    public List<Coupon> findCouponByUser(@PathVariable("user-id") long userId) {
        return couponService.findCouponByUserId(userId);
    }

    @PutMapping("/coupon/use/{serial-number}")
    public Coupon useCoupon(@PathVariable("serial-number") String serialNumber) {
        return couponService.useCoupon(serialNumber);
    }

    @PutMapping("/coupon/use-cancel/{serial-number}")
    public Coupon useCancelCoupon(@PathVariable("serial-number") String serialNumber) {
        return couponService.useCancelCoupon(serialNumber);
    }

    @GetMapping("/coupon/expired-today")
    public List<Coupon> getExpiredTodayCoupon() {
        return couponService.getExpiredTodayCoupon();
    }
}
