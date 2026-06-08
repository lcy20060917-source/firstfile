package com.ecommerce.service.impl;

import com.ecommerce.common.Constants;
import com.ecommerce.common.JwtUtil;
import com.ecommerce.domain.User;
import com.ecommerce.domain.dto.LoginRequest;
import com.ecommerce.domain.dto.RegisterRequest;
import com.ecommerce.exception.BusinessException;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.UserService;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User register(RegisterRequest request) {
        User existing = userRepository.findByUsername(request.getUsername());
        if (existing != null) {
            throw new BusinessException(409, "用户名已被占用");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(new Sha256Hash(request.getPassword()).toHex());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole("USER");
        user.setStatus(Constants.UserStatus.ACTIVE);

        userRepository.insert(user);
        log.info("用户注册成功: username={}", user.getUsername());
        return user;
    }

    @Override
    public String login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            throw new BusinessException(401, "用户名或密码错误");
        }
        if (user.getStatus() == Constants.UserStatus.DISABLED) {
            throw new BusinessException(403, "账号已被禁用");
        }
        String inputHash = new Sha256Hash(request.getPassword()).toHex();
        if (!inputHash.equals(user.getPassword())) {
            throw new BusinessException(401, "用户名或密码错误");
        }
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        log.info("用户登录成功: username={}", user.getUsername());
        return token;
    }

    @Override
    public User findById(Long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        return user;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
