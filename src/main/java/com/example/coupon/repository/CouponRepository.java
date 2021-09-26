package com.example.coupon.repository;

import com.example.coupon.entity.Coupon;
import com.example.coupon.entity.CouponMeta;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjuster;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CouponRepository {
    private final EntityManager em;

    public CouponRepository(EntityManager em) {
        this.em = em;
    }

    public Coupon save(Coupon coupon) {
        coupon.setUpdatedAt(LocalDateTime.now());
        em.persist(coupon);
        return coupon;
    }

    public List<Coupon> findAll() {
        return em.createQuery("select m from Coupon m", Coupon.class)
                .getResultList();
    }

    public List<Coupon> findByUserid(long userId) {
         return em.createQuery("select m from Coupon m where m.userId=:userId", Coupon.class)
                 .setParameter("userId", userId)
                 .getResultList();
    }

    public Optional<Coupon> findBySerialNumber(String serialNumber) {
        List<Coupon> list = em.createQuery("select m from Coupon m where m.serialNumber=:serialNumber", Coupon.class)
                .setParameter("serialNumber", serialNumber)
                .getResultList();
        if (list.size() == 0) return Optional.empty();
        return Optional.ofNullable(list.get(0));
    }

    public List<Coupon> findAllExpiredToday() {
        System.out.println(LocalDateTime.now().with(LocalTime.MIN));
        return em.createQuery("select m from Coupon m where m.expiredAt>=:start AND m.expiredAt<:end", Coupon.class)
                .setParameter("start", LocalDateTime.now().with(LocalTime.MIN))
                .setParameter("end", LocalDateTime.now().plusDays(1).with(LocalTime.MIN))
                .getResultList();
    }

}
