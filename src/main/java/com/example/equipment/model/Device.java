package com.example.equipment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "device")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "laboratory_id", nullable = false)
    private Long laboratoryId;

    @Column(name = "class_name")
    private String className;

    @Column(name = "device_name", nullable = false)
    private String deviceName;

    @Column(name = "device_code", nullable = false, unique = true)
    private String deviceCode;

    @Column(name = "serial_no")
    private String serialNo;

    @Column(name = "spec")
    private String spec;

    @Column(name = "unit_price")
    private java.math.BigDecimal unitPrice;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "warranty_until")
    private LocalDate warrantyUntil;

    @Column(name = "handler_user_id")
    private Long handlerUserId;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}


