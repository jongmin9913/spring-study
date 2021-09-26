package com.example.coupon.service;

import com.example.coupon.entity.CouponMeta;
import com.example.coupon.repository.CouponMetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CouponMetaService {
    private final CouponMetaRepository couponMetaRepository;

    @Autowired
    public CouponMetaService(CouponMetaRepository couponMetaRepository) {
        this.couponMetaRepository = couponMetaRepository;
    }

    public List<CouponMeta> getList() {
        return couponMetaRepository.findAll();
    }

    public CouponMeta add(CouponMeta meta) {
        Optional<CouponMeta> old = couponMetaRepository.findByCode(meta.getCode());
        if (old.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "duplicate code: " + meta.getCode());
        }

        if (meta.getExpiredAt() == null && meta.getDuration() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "expiredAt or duration must set");
        }

        meta.setCreatedAt(LocalDateTime.now());
        meta.setUpdatedAt(LocalDateTime.now());
        return couponMetaRepository.save(meta);
    }

    public Optional<CouponMeta> findByCode(String code) {
        return couponMetaRepository.findByCode(code);
    }

    public CouponMeta update(String code, CouponMeta meta) {
        Optional<CouponMeta> old = findByCode(code);
        if (old.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not exist code: " + code);
        }

        if (meta.getExpiredAt() == null && meta.getDuration() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "expiredAt or duration must set");
        }

        CouponMeta oldObj = old.get();
        oldObj.setDuration(meta.getDuration());
        oldObj.setExpiredAt(meta.getExpiredAt());
        oldObj.setUpdatedAt(LocalDateTime.now());
        return couponMetaRepository.save(oldObj);
    }
}
