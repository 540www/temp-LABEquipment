package com.example.equipment.web;

import com.example.equipment.model.*;
import com.example.equipment.repository.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reference")
public class ReferenceDataController {

    private final DeviceCategoryRepository deviceCategoryRepository;
    private final LaboratoryRepository laboratoryRepository;
    private final UserAccountRepository userAccountRepository;
    private final DepartmentRepository departmentRepository;
    private final DeviceRepository deviceRepository;

    public ReferenceDataController(
            DeviceCategoryRepository deviceCategoryRepository,
            LaboratoryRepository laboratoryRepository,
            UserAccountRepository userAccountRepository,
            DepartmentRepository departmentRepository,
            DeviceRepository deviceRepository) {
        this.deviceCategoryRepository = deviceCategoryRepository;
        this.laboratoryRepository = laboratoryRepository;
        this.userAccountRepository = userAccountRepository;
        this.departmentRepository = departmentRepository;
        this.deviceRepository = deviceRepository;
    }

    @GetMapping("/categories")
    public List<DeviceCategory> getCategories() {
        return deviceCategoryRepository.findAll();
    }

    @GetMapping("/laboratories")
    public List<Laboratory> getLaboratories() {
        return laboratoryRepository.findAll();
    }

    @GetMapping("/users")
    public List<UserAccount> getUsers() {
        return userAccountRepository.findAll();
    }

    @GetMapping("/departments")
    public List<Department> getDepartments() {
        return departmentRepository.findAll();
    }

    @GetMapping("/devices")
    public List<Device> getDevices() {
        return deviceRepository.findAll();
    }

    @GetMapping("/all")
    public Map<String, Object> getAll() {
        Map<String, Object> data = new HashMap<>();
        data.put("categories", deviceCategoryRepository.findAll());
        data.put("laboratories", laboratoryRepository.findAll());
        data.put("users", userAccountRepository.findAll());
        data.put("departments", departmentRepository.findAll());
        data.put("devices", deviceRepository.findAll());
        return data;
    }
}

