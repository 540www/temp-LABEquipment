package com.example.equipment.web;

import com.example.equipment.model.Device;
import com.example.equipment.repository.DeviceRepository;
import com.example.equipment.web.dto.DeviceDtos;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    private final DeviceRepository deviceRepository;

    public DeviceController(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @PostMapping
    public ResponseEntity<Device> create(@RequestBody @Valid DeviceDtos.Create req) {
        try {
            deviceRepository.findByDeviceCode(req.deviceCode()).ifPresent(d -> {
                throw new IllegalArgumentException("设备编号已存在: " + req.deviceCode());
            });
            Device d = new Device();
            d.setCategoryId(req.categoryId());
            d.setLaboratoryId(req.laboratoryId());
            d.setClassName(req.className());
            d.setDeviceName(req.deviceName());
            d.setDeviceCode(req.deviceCode());
            d.setSerialNo(req.serialNo());
            d.setSpec(req.spec());
            d.setUnitPrice(req.unitPrice());
            d.setQuantity(req.quantity() == null ? 1 : req.quantity());
            d.setStatus("IN_STOCK");
            d.setCreatedAt(LocalDateTime.now());
            d.setUpdatedAt(LocalDateTime.now());
            Device saved = deviceRepository.save(d);
            return ResponseEntity.created(URI.create("/devices/" + saved.getId())).body(saved);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("创建设备失败: " + e.getMessage(), e);
        }
    }

    @GetMapping
    public List<Device> list() {
        return deviceRepository.findAll();
    }

    @GetMapping("/{id}")
    public Device get(@PathVariable Long id) {
        return deviceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("device not found"));
    }

    @PatchMapping("/{id}/status")
    public Device updateStatus(@PathVariable Long id, @RequestBody @Valid DeviceDtos.UpdateStatus req) {
        Device d = deviceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("device not found"));
        d.setStatus(req.status());
        d.setUpdatedAt(LocalDateTime.now());
        return deviceRepository.save(d);
    }
}


