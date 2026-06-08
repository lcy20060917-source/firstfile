package com.ecommerce.service;

import com.ecommerce.domain.User;
import com.ecommerce.domain.dto.LoginRequest;
import com.ecommerce.domain.dto.RegisterRequest;

public interface UserService {

        User register(RegisterRequest request);

        String login(LoginRequest request);

        User findById(Long id);

        User findByUsername(String username);
}
