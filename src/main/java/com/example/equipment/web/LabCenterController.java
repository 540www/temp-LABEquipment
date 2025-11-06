package com.example.equipment.web;

import com.example.equipment.model.Department;
import com.example.equipment.model.Laboratory;
import com.example.equipment.model.UserAccount;
import com.example.equipment.repository.DepartmentRepository;
import com.example.equipment.repository.LaboratoryRepository;
import com.example.equipment.repository.UserAccountRepository;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/lab-center")
public class LabCenterController {

    private final DepartmentRepository departmentRepository;
    private final LaboratoryRepository laboratoryRepository;
    private final UserAccountRepository userAccountRepository;

    public LabCenterController(DepartmentRepository departmentRepository, LaboratoryRepository laboratoryRepository, UserAccountRepository userAccountRepository) {
        this.departmentRepository = departmentRepository;
        this.laboratoryRepository = laboratoryRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @PostMapping("/setup")
    public Map<String, Object> setup(@RequestParam @NotBlank String username,
                                     @RequestParam @NotBlank String password,
                                     @RequestParam @NotBlank String labName,
                                     @RequestParam @NotBlank String departmentName) {
        Department dept = new Department();
        dept.setName(departmentName);
        dept.setCreatedAt(LocalDateTime.now());
        dept.setUpdatedAt(LocalDateTime.now());
        dept = departmentRepository.save(dept);

        UserAccount user = new UserAccount();
        user.setUsername(username);
        user.setPassword(password);
        user.setFullName(username);
        user.setDepartmentId(dept.getId());
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user = userAccountRepository.save(user);

        Laboratory lab = new Laboratory();
        lab.setName(labName);
        lab.setDepartmentId(dept.getId());
        lab.setManagerUserId(user.getId());
        lab.setCreatedAt(LocalDateTime.now());
        lab.setUpdatedAt(LocalDateTime.now());
        lab = laboratoryRepository.save(lab);

        Map<String, Object> res = new HashMap<>();
        res.put("departmentId", dept.getId());
        res.put("laboratoryId", lab.getId());
        res.put("userId", user.getId());
        return res;
    }
}


