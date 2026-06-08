package com.ecommerce.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);

    private final DataSource dataSource;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    public DatabaseInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) {
        if (driverClassName != null && driverClassName.contains("sqlite")) {
            try {
                log.info("开始初始化 SQLite 数据库...");

                ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
                populator.addScript(new ClassPathResource("db/schema-sqlite.sql"));
                populator.addScript(new ClassPathResource("db/data.sql"));
                populator.setContinueOnError(true);
                populator.execute(dataSource);

                log.info("SQLite 数据库初始化完成");
            } catch (Exception e) {
                log.warn("数据库初始化警告: {}", e.getMessage());
            }
        }
    }
}
