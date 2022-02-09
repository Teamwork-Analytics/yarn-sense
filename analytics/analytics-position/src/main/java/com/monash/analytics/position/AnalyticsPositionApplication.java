package com.monash.analytics.position;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.monash.analytics"})
@MapperScan(basePackages = {"com.monash.analytics.position.dao"})
@EnableDiscoveryClient
@SpringBootApplication
public class AnalyticsPositionApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnalyticsPositionApplication.class, args);
    }

}
