package com.example.coupon.repository;

import com.example.coupon.entity.CouponMeta;
import com.example.coupon.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserRepositoryTest {

    @Autowired UserRepository repository;

    @Test
    void test1() {
        User user = new User();
        user.setName("유저1");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        repository.save(user);

        assertThat(user.getId()).isGreaterThan(0);

        List<User> list = repository.findAll();
        assertThat(list.size()).isGreaterThan(0);

        Optional<User> result = repository.findById(user.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(user.getId());

        User user2 = new User();
        user2.setName(user.getName());
        user2.setCreatedAt(LocalDateTime.now());
        user2.setUpdatedAt(LocalDateTime.now());

        assertThrows(DataIntegrityViolationException.class, () -> repository.save(user2));
    }
}
