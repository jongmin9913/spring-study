package com.example.coupon.controller;

import com.example.coupon.entity.CouponMeta;
import com.example.coupon.service.CouponMetaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class CouponMetaController {
    private final CouponMetaService couponMetaService;

    public CouponMetaController(CouponMetaService couponMetaService) {
        this.couponMetaService = couponMetaService;
    }

    @GetMapping("/coupon-meta")
    public List<CouponMeta> getList() {
        return couponMetaService.getList();
    }

    @PostMapping("/coupon-meta")
    public CouponMeta add(@RequestBody CouponMeta meta) {
        return couponMetaService.add(meta);
    }

    @PutMapping("/coupon-meta/{code}")
    public CouponMeta update(@PathVariable("code") String code, @RequestBody CouponMeta meta) {
        return couponMetaService.update(code, meta);
    }

    @GetMapping("/coupon-meta/{code}")
    public CouponMeta findByCode(@PathVariable("code") String code) {
        Optional<CouponMeta> meta = couponMetaService.findByCode(code);
        if (meta.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not exist: " + code);
        }

        return meta.get();
    }
}
