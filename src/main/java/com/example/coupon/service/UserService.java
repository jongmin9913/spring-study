package com.example.coupon.service;

import com.example.coupon.entity.CouponMeta;
import com.example.coupon.entity.User;
import com.example.coupon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getList() {
        return userRepository.findAll();
    }

    public User add(User user) {
        Optional<User> old = findByName(user.getName());
        if (old.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "duplicate name: " + user.getName());
        }

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    public User update(long id, User user) {
        Optional<User> old = findById(id);
        if (old.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not exist id: " + id);
        }

        if (!user.getName().equals(old.get().getName())) {
            System.out.println(user.getName() + " / " + old.get().getName());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name can't modify");
        }

        User oldObj = old.get();
        oldObj.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(oldObj);
    }
}
