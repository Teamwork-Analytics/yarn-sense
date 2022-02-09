package com.monash.analytics.video;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.monash.analytics"})
@MapperScan(basePackages = {"com.monash.analytics.video.dao"})
@EnableDiscoveryClient
@SpringBootApplication
public class AnalyticsVideoApplication {

    public static void main(String[] args) {
//        SpringApplication.run(AnalyticsVideoApplication.class, args);
        SpringApplicationBuilder builder = new SpringApplicationBuilder(AnalyticsVideoApplication.class);
        builder.headless(false).run(args);
    }

}
