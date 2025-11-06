package com.example.equipment.config;

import com.example.equipment.model.UserAccount;
import com.example.equipment.repository.UserAccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    public CustomUserDetailsService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount account = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));

        if (Boolean.FALSE.equals(account.getEnabled())) {
            throw new UsernameNotFoundException("用户已被禁用: " + username);
        }

        // 如果密码没有 {noop} 前缀，添加它（用于明文密码）
        String password = account.getPassword();
        if (!password.startsWith("{") && !password.contains("}")) {
            password = "{noop}" + password;
        }

        return User.withUsername(account.getUsername())
                .password(password)
                .roles("USER")
                .disabled(!Boolean.TRUE.equals(account.getEnabled()))
                .build();
    }
}

