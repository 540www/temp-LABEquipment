package com.example.equipment.web;

import com.example.equipment.model.BorrowTransaction;
import com.example.equipment.model.Device;
import com.example.equipment.repository.BorrowTransactionRepository;
import com.example.equipment.repository.DeviceRepository;
import com.example.equipment.web.dto.BorrowDtos;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/borrows")
public class BorrowController {

    private final BorrowTransactionRepository borrowRepository;
    private final DeviceRepository deviceRepository;

    public BorrowController(BorrowTransactionRepository borrowRepository, DeviceRepository deviceRepository) {
        this.borrowRepository = borrowRepository;
        this.deviceRepository = deviceRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<BorrowTransaction> borrow(@RequestBody @Valid BorrowDtos.Create req) {
        try {
            if (req.dueAt().isBefore(req.borrowedAt())) {
                throw new IllegalArgumentException("应还时间不能早于借出时间");
            }
            Device device = deviceRepository.findById(req.deviceId())
                    .orElseThrow(() -> new IllegalArgumentException("设备不存在，ID: " + req.deviceId()));
            if (!"IN_STOCK".equals(device.getStatus())) {
                throw new IllegalArgumentException("设备不可借出，当前状态: " + device.getStatus());
            }
            if (!borrowRepository.findByDeviceIdAndReturnedAtIsNull(req.deviceId()).isEmpty()) {
                throw new IllegalArgumentException("设备已被借出，请先归还");
            }
            BorrowTransaction tx = new BorrowTransaction();
            tx.setDeviceId(req.deviceId());
            tx.setBorrowerUserId(req.borrowerUserId());
            tx.setBorrowedAt(req.borrowedAt());
            tx.setDueAt(req.dueAt());
            tx.setCheckoutOperatorId(req.checkoutOperatorId());
            tx.setStatus("BORROWED");
            BorrowTransaction saved = borrowRepository.save(tx);

            device.setStatus("IN_USE");
            device.setUpdatedAt(LocalDateTime.now());
            deviceRepository.save(device);

            return ResponseEntity.created(URI.create("/borrows/" + saved.getId())).body(saved);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("借出失败: " + e.getMessage(), e);
        }
    }

    @PostMapping("/{id}/return")
    @Transactional
    public BorrowTransaction doReturn(@PathVariable Long id, @RequestBody @Valid BorrowDtos.Return req) {
        try {
            BorrowTransaction tx = borrowRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("借还记录不存在，ID: " + id));
            if (tx.getReturnedAt() != null) {
                throw new IllegalArgumentException("该记录已归还，无需重复操作");
            }
            tx.setReturnedAt(req.returnedAt());
            tx.setStatus(req.returnedAt().isAfter(tx.getDueAt()) ? "OVERDUE" : "RETURNED");
            tx.setReturnOperatorId(req.returnOperatorId());
            BorrowTransaction saved = borrowRepository.save(tx);

            Device device = deviceRepository.findById(tx.getDeviceId())
                    .orElseThrow(() -> new IllegalArgumentException("设备不存在，ID: " + tx.getDeviceId()));
            device.setStatus("IN_STOCK");
            device.setUpdatedAt(LocalDateTime.now());
            deviceRepository.save(device);
            return saved;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("归还失败: " + e.getMessage(), e);
        }
    }

    @GetMapping
    public List<BorrowTransaction> list() {
        return borrowRepository.findAll();
    }
}


