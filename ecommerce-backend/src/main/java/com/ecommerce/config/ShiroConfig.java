package com.ecommerce.config;

import com.ecommerce.common.JwtUtil;
import com.ecommerce.config.shiro.JwtFilter;
import com.ecommerce.config.shiro.UserRealm;
import com.ecommerce.repository.UserRepository;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public DefaultWebSecurityManager securityManager(Realm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);

        return securityManager;
    }

    @Bean
    public Realm realm(UserRepository userRepository) {
        return new UserRealm(userRepository);
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager,
                                                          JwtUtil jwtUtil) {
        ShiroFilterFactoryBean factory = new ShiroFilterFactoryBean();
        factory.setSecurityManager(securityManager);
        Map<String, Filter> filters = new HashMap<>();
        filters.put("jwt", new JwtFilter(jwtUtil));
        factory.setFilters(filters);
        Map<String, String> filterChain = new LinkedHashMap<>();
        filterChain.put("/api/auth/login", "anon");
        filterChain.put("/api/auth/register", "anon");
        filterChain.put("/api/products/**", "anon");
        filterChain.put("/api/cart/**", "jwt");
        filterChain.put("/api/orders/**", "jwt");
        filterChain.put("/api/user/**", "jwt");
        filterChain.put("/**", "anon");

        factory.setFilterChainDefinitionMap(filterChain);
        return factory;
    }
}
