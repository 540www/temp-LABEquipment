package com.example.equipment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "borrow_transaction")
public class BorrowTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false)
    private Long deviceId;

    @Column(name = "borrower_user_id", nullable = false)
    private Long borrowerUserId;

    @Column(name = "borrowed_at", nullable = false)
    private LocalDateTime borrowedAt;

    @Column(name = "due_at", nullable = false)
    private LocalDateTime dueAt;

    @Column(name = "returned_at")
    private LocalDateTime returnedAt;

    @Column(name = "status")
    private String status;

    @Column(name = "checkout_operator_id")
    private Long checkoutOperatorId;

    @Column(name = "return_operator_id")
    private Long returnOperatorId;
}


