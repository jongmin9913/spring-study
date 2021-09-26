package com.example.coupon.repository;

import com.example.coupon.entity.CouponMeta;
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
public class CouponMetaRepositoryTest {

    @Autowired CouponMetaRepository repository;

    @Test
    void test1() {
        CouponMeta meta = new CouponMeta();
        meta.setCode("test1");
        meta.setName("테스트1");
        meta.setExpiredAt(null);
        meta.setDuration(0);
        meta.setCreatedAt(LocalDateTime.now());
        meta.setUpdatedAt(LocalDateTime.now());
        repository.save(meta);

        assertThat(meta.getId()).isGreaterThan(0);

        List<CouponMeta> list = repository.findAll();
        assertThat(list.size()).isGreaterThan(0);

        Optional<CouponMeta> result = repository.findByCode(meta.getCode());
        assertThat(result.isPresent()).isTrue();
    }
}
