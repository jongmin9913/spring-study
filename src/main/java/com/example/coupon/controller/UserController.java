package com.example.coupon.controller;

import com.example.coupon.entity.CouponMeta;
import com.example.coupon.entity.User;
import com.example.coupon.service.CouponMetaService;
import com.example.coupon.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public List<User> getList() {
        return userService.getList();
    }

    @PostMapping("/user")
    public User add(@RequestBody User user) {
        return userService.add(user);
    }

    @PutMapping("/user/{id}")
    public User update(@PathVariable("id") long id, @RequestBody User user) {
        return userService.update(id, user);
    }

    @GetMapping("/user/find-by-name/{name}")
    public User findByName(@PathVariable("name") String name) {
        Optional<User> meta = userService.findByName(name);
        if (meta.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not exist name: " + name);
        }

        return meta.get();
    }
}
