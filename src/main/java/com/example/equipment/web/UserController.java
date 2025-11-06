package com.example.equipment.web;

import com.example.equipment.model.UserAccount;
import com.example.equipment.repository.DepartmentRepository;
import com.example.equipment.repository.UserAccountRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserAccountRepository userAccountRepository;
    private final DepartmentRepository departmentRepository;

    public UserController(UserAccountRepository userAccountRepository, DepartmentRepository departmentRepository) {
        this.userAccountRepository = userAccountRepository;
        this.departmentRepository = departmentRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest req) {
        // 检查用户名是否已存在
        if (userAccountRepository.findByUsername(req.username()).isPresent()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "USERNAME_EXISTS");
            error.put("message", "用户名已存在");
            return ResponseEntity.badRequest().body(error);
        }

        // 检查邮箱是否已存在（如果提供了邮箱）
        if (req.email() != null && !req.email().isEmpty()) {
            boolean emailExists = userAccountRepository.findAll().stream()
                    .anyMatch(u -> req.email().equals(u.getEmail()));
            if (emailExists) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "EMAIL_EXISTS");
                error.put("message", "邮箱已被注册");
                return ResponseEntity.badRequest().body(error);
            }
        }

        // 验证部门ID是否存在（如果提供了）
        if (req.departmentId() != null) {
            departmentRepository.findById(req.departmentId())
                    .orElseThrow(() -> new IllegalArgumentException("部门不存在，ID: " + req.departmentId()));
        }

        // 创建新用户
        UserAccount user = new UserAccount();
        user.setUsername(req.username());
        // 使用 {noop} 前缀表示明文密码（Spring Security 格式）
        // 注意：生产环境应使用 BCrypt 等加密方式
        String password = req.password();
        if (!password.startsWith("{") && !password.contains("}")) {
            password = "{noop}" + password;
        }
        user.setPassword(password);
        user.setFullName(req.fullName());
        user.setEmail(req.email());
        user.setDepartmentId(req.departmentId());
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        UserAccount saved = userAccountRepository.save(user);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "注册成功");
        result.put("userId", saved.getId());
        result.put("username", saved.getUsername());

        return ResponseEntity.ok(result);
    }

    public record RegisterRequest(
            @NotBlank @Size(min = 3, max = 20) String username,
            @NotBlank @Size(min = 6, max = 50) String password,
            String fullName,
            @Email String email,
            Long departmentId
    ) {}
}

