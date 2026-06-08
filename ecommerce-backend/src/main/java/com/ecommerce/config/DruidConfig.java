package com.ecommerce.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DruidConfig {

    @Bean
    public ServletRegistrationBean<StatViewServlet> druidStatViewServlet() {
        ServletRegistrationBean<StatViewServlet> registration = new ServletRegistrationBean<>(
                new StatViewServlet(), "/druid/*");
        registration.addInitParameter("loginUsername", "admin");
        registration.addInitParameter("loginPassword", "admin123");
        registration.addInitParameter("resetEnable", "false");
        return registration;
    }

    @Bean
    public FilterRegistrationBean<WebStatFilter> druidWebStatFilter() {
        FilterRegistrationBean<WebStatFilter> registration = new FilterRegistrationBean<>(new WebStatFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return registration;
    }
}
