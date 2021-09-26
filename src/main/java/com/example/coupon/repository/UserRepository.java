package com.example.coupon.repository;

import com.example.coupon.entity.Coupon;
import com.example.coupon.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private final EntityManager em;

    public UserRepository(EntityManager em) {
        this.em = em;
    }

    public User save(User user) {
        em.persist(user);
        return user;
    }

    public List<User> findAll() {
        return em.createQuery("select m from User m", User.class)
                .getResultList();
    }

    public Optional<User> findByName(String name) {
         List<User> list = em.createQuery("select m from User m where m.name=:name", User.class)
                 .setParameter("name", name)
                 .getResultList();
         if (list.size() == 0) return Optional.empty();
         return Optional.ofNullable(list.get(0));
    }

    public Optional<User> findById(long id) {
        User user = em.find(User.class, id);
        if (user == null) return Optional.empty();

        return Optional.ofNullable(user);
    }
}
