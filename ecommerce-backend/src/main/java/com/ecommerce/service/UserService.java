package com.ecommerce.service;

import com.ecommerce.dto.LoginRequest;
import com.ecommerce.dto.RegisterRequest;
import com.ecommerce.entity.User;

public interface UserService {
    User register(RegisterRequest request);
    String login(LoginRequest request);
    User findById(Long id);
    User findByUsername(String username);
}
