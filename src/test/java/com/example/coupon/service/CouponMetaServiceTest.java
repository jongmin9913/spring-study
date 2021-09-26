package com.example.coupon.service;

import com.example.coupon.entity.CouponMeta;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CouponMetaServiceTest {
    CouponMetaService service;

    @Autowired
    public CouponMetaServiceTest(CouponMetaService service) {
        this.service = service;
    }

    @BeforeEach
    public void beforeEach() {
        System.out.println("beforeEach");
        CouponMeta meta = new CouponMeta();
        meta.setCode("test1");
        meta.setName("테스트1");
        meta.setExpiredAt(null);
        meta.setDuration(0);
        meta.setCreatedAt(LocalDateTime.now());
        meta.setUpdatedAt(LocalDateTime.now());
        service.add(meta);

        CouponMeta meta1 = new CouponMeta();
        meta1.setCode("test2");
        meta1.setName("테스트2");
        meta1.setExpiredAt(null);
        meta1.setDuration(0);
        meta1.setCreatedAt(LocalDateTime.now());
        meta1.setUpdatedAt(LocalDateTime.now());
        service.add(meta1);
    }

    @Test
    void duplicateAdd() {

        CouponMeta meta2 = new CouponMeta();
        meta2.setCode("test2");
        meta2.setName("테스트2");
        meta2.setExpiredAt(null);
        meta2.setDuration(0);
        meta2.setCreatedAt(LocalDateTime.now());
        meta2.setUpdatedAt(LocalDateTime.now());
        assertThrows(ResponseStatusException.class, () -> service.add(meta2));
    }

    @Test
    void getList() {
        List<CouponMeta> list = service.getList();
        assertThat(list.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    void findByCode() {
        Optional<CouponMeta> meta = service.findByCode("test1");
        assertThat(meta.isPresent()).isTrue();
    }

    @Test
    void update() {
        CouponMeta meta = service.findByCode("test1").get();
        meta.setDuration(10);
        meta.setExpiredAt(LocalDateTime.now().plusDays(7));
        service.update(meta.getCode(), meta);

        Optional<CouponMeta> find = service.findByCode(meta.getCode());
        assertThat(find.isPresent()).isTrue();
        assertThat(find.get().getDuration()).isEqualTo(meta.getDuration());
        assertThat(find.get().getExpiredAt()).isEqualTo(meta.getExpiredAt());
    }
}
