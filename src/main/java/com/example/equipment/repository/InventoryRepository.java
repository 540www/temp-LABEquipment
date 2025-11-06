package com.example.equipment.repository;

import com.example.equipment.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByItemIdAndLaboratoryId(Long itemId, Long laboratoryId);
}


