package com.example.equipment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "item_id", nullable = false)
    private Long itemId;
    @Column(name = "laboratory_id")
    private Long laboratoryId;
    private Integer quantity;
    @Column(name = "safety_stock")
    private Integer safetyStock;
}


