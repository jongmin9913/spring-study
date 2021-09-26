package com.example.coupon.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "coupon_meta")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CouponMeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "expired_at", nullable = true, length = 100)
    private LocalDateTime expiredAt;

    @Column(name = "duration")
    private int duration;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
