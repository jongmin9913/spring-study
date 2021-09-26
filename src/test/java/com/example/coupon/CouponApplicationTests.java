package com.example.coupon;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;

@SpringBootTest
public class CouponApplicationTests {

    @Autowired
    private EntityManager em;

    @Test
    void contextLoads() {
        System.out.println(em.toString());
    }

}
