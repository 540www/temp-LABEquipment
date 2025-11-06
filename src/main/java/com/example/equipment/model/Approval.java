package com.example.equipment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "approval")
public class Approval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "biz_type")
    private String bizType;

    @Column(name = "biz_id")
    private Long bizId;

    private String node;

    @Column(name = "approver_user_id")
    private Long approverUserId;

    private String result; // PENDING/APPROVED/REJECTED

    private String comment;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "acted_at")
    private LocalDateTime actedAt;
}


