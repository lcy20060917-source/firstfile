package com.ecommerce.repository.impl;

import com.ecommerce.domain.User;
import com.ecommerce.repository.UserRepository;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SqlSessionTemplate sqlSession;

    public UserRepositoryImpl(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    private static final String NAMESPACE = "com.ecommerce.repository.UserRepository.";

    @Override
    public User findByUsername(String username) {
        return sqlSession.selectOne(NAMESPACE + "findByUsername", username);
    }

    @Override
    public User findById(Long id) {
        return sqlSession.selectOne(NAMESPACE + "findById", id);
    }

    @Override
    public int insert(User user) {
        return sqlSession.insert(NAMESPACE + "insert", user);
    }

    @Override
    public int update(User user) {
        return sqlSession.update(NAMESPACE + "update", user);
    }

    @Override
    public int deleteById(Long id) {
        return sqlSession.delete(NAMESPACE + "deleteById", id);
    }
}
