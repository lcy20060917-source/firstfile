package com.ecommerce.repository;

import com.ecommerce.domain.User;

public interface UserRepository {

        User findByUsername(String username);

        User findById(Long id);

        int insert(User user);

        int update(User user);

        int deleteById(Long id);
}
