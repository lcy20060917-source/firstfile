package com.ecommerce.repository;

import com.ecommerce.domain.User;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserRepository 切片测试（@MybatisTest）。
 *
 * <p>使用嵌入式数据库或 SQLite 进行测试。</p>
 *
 * @author ecommerce-dev
 */
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("dev")
@Import({com.ecommerce.repository.impl.UserRepositoryImpl.class})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    /**
     * 应该成功插入并查询用户。
     */
    @Test
    void should_insert_and_find_user() {
        User user = new User();
        user.setUsername("testrepo");
        user.setPassword("encoded123");
        user.setRole("USER");
        user.setStatus(1);

        int rows = userRepository.insert(user);
        assertTrue(rows > 0, "插入应返回受影响行数");
        assertNotNull(user.getId(), "插入后应回填主键");

        User found = userRepository.findByUsername("testrepo");
        assertNotNull(found, "应能查到刚插入的用户");
        assertEquals("testrepo", found.getUsername());
    }

    /**
     * 查询不存在的用户应返回 null。
     */
    @Test
    void should_return_null_when_user_not_found() {
        User user = userRepository.findByUsername("nonexistent");
        assertNull(user, "不存在的用户应返回 null");
    }
}
