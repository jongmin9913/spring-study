package com.example.coupon.repository;

import com.example.coupon.entity.CouponMeta;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class CouponMetaRepository {
    private final EntityManager em;

    public CouponMetaRepository(EntityManager em) {
        this.em = em;
    }

    public CouponMeta save(CouponMeta meta) {
        em.persist(meta);
        return meta;
    }

    public List<CouponMeta> findAll() {
        return em.createQuery("select m from CouponMeta m", CouponMeta.class)
                .getResultList();
    }

    public Optional<CouponMeta> findByCode(String code) {
         List<CouponMeta> list = em.createQuery("select m from CouponMeta m where m.code=:code", CouponMeta.class)
                 .setParameter("code", code)
                 .getResultList();
         if (list.size() == 0) return Optional.empty();
         return Optional.ofNullable(list.get(0));
    }
}
