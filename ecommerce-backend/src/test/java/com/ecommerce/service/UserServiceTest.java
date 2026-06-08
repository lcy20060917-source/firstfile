package com.ecommerce.service;

import com.ecommerce.common.JwtUtil;
import com.ecommerce.domain.User;
import com.ecommerce.domain.dto.LoginRequest;
import com.ecommerce.domain.dto.RegisterRequest;
import com.ecommerce.exception.BusinessException;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.impl.UserServiceImpl;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * UserService 单元测试。
 *
 * @author ecommerce-dev
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userService;

    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "123456";

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername(TEST_USERNAME);
        registerRequest.setPassword(TEST_PASSWORD);
        registerRequest.setEmail("test@test.com");

        loginRequest = new LoginRequest();
        loginRequest.setUsername(TEST_USERNAME);
        loginRequest.setPassword(TEST_PASSWORD);
    }

    /**
     * 应该成功注册新用户。
     */
    @Test
    void should_register_user_when_username_not_exists() {
        when(userRepository.findByUsername(anyString())).thenReturn(null);
        when(userRepository.insert(any(User.class))).thenReturn(1);

        User user = userService.register(registerRequest);

        assertNotNull(user);
        assertEquals(TEST_USERNAME, user.getUsername());
        assertEquals("USER", user.getRole());
    }

    /**
     * 用户名已存在时应抛出异常。
     */
    @Test
    void should_throw_exception_when_username_exists() {
        User existing = new User();
        existing.setUsername(TEST_USERNAME);
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(existing);

        assertThrows(BusinessException.class, () -> userService.register(registerRequest));
    }

    /**
     * 应该成功登录并返回 Token。
     */
    @Test
    void should_return_token_when_login_success() {
        User user = new User();
        user.setId(1L);
        user.setUsername(TEST_USERNAME);
        user.setPassword(new Sha256Hash(TEST_PASSWORD).toHex());
        user.setStatus(1);

        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(user);
        when(jwtUtil.generateToken(any(), any())).thenReturn("mock-jwt-token");

        String token = userService.login(loginRequest);
        assertEquals("mock-jwt-token", token);
    }
}
