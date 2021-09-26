package com.example.coupon.repository;

import com.example.coupon.entity.Coupon;
import com.example.coupon.entity.CouponMeta;
import com.example.coupon.entity.CouponStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class CouponRepositoryTest {

    @Autowired CouponRepository repository;

    @Test
    void test1() {
        Coupon coupon = new Coupon();
        coupon.setCouponCode("test-code");
        coupon.setStatus(CouponStatus.NONE);
        coupon.setSerialNumber("serial-0000-0001");
        coupon.setUserId(1);
        coupon.setExpiredAt(LocalDateTime.now());
        coupon.setCreatedAt(LocalDateTime.now());
        coupon.setUpdatedAt(LocalDateTime.now());
        repository.save(coupon);

        assertThat(coupon.getId()).isGreaterThan(0);

        List<Coupon> list = repository.findAll();
        assertThat(list.size()).isGreaterThan(0);

        Optional<Coupon> result = repository.findByUserid(coupon.getUserId());
        assertThat(result.isPresent()).isTrue();

        List<Coupon> findList = repository.findAllExpiredToday();

    }
}
