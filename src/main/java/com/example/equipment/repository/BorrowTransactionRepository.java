package com.example.equipment.repository;

import com.example.equipment.model.BorrowTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowTransactionRepository extends JpaRepository<BorrowTransaction, Long> {
    List<BorrowTransaction> findByDeviceIdAndReturnedAtIsNull(Long deviceId);
}


