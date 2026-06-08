package com.ecommerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @PostConstruct
    public void init() {
        if (driverClassName.contains("sqlite")) {
            System.out.println("========================================");
            System.out.println("  数据库类型: SQLite");
            System.out.println("  注意: SQLite 不支持高并发写入");
            System.out.println("========================================");
        } else if (driverClassName.contains("mysql")) {
            System.out.println("========================================");
            System.out.println("  数据库类型: MySQL");
            System.out.println("========================================");
        }
    }
}
