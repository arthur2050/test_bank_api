package com.bank.api.controller;

import com.bank.api.entity.User;
import com.bank.api.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TestController {

    private final UserRepository userRepository;

    public TestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/test")
    public String insertTestUser() {
        User user = new User();
        user.setUsername("testuser_" + System.currentTimeMillis());
        user.setPassword("123456"); // просто для теста, без шифрования!
        user.setEnabled(true);
        userRepository.save(user);
        return "Test user inserted!";
    }

    @GetMapping("/test")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 🔐 Защищённый эндпоинт
    @GetMapping("/api/secure/test")
    public String securedEndpoint() {
        return "Endpoint is secured!";
    }
}
