package com.xuanluan.mc.org;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Xuan Luan
 * @createdAt 1/2/2023
 */
@EnableJpaRepositories(basePackages = {"com.xuanluan.mc.repository.sequence", "com.xuanluan.mc.org"})
@EntityScan(basePackages = {"com.xuanluan.mc.domain.entity", "com.xuanluan.mc.org"})
@ComponentScan(basePackages = {"com.xuanluan.mc.service", "com.xuanluan.mc.org"})
@SpringBootApplication
@EnableEurekaClient
public class OrganizationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrganizationServiceApplication.class, args);
    }
}
