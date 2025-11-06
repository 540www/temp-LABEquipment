package com.example.equipment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "consumable")
public class Consumable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;
    private String spec;
    private String unit;
    @Column(name = "supplier_id")
    private Long supplierId;
    @Column(name = "unit_price")
    private java.math.BigDecimal unitPrice;
}


