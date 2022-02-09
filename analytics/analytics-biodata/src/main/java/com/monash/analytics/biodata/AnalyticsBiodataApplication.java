package com.monash.analytics.biodata;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.monash.analytics"})
@MapperScan(basePackages = {"com.monash.analytics.biodata.dao"})
@EnableDiscoveryClient
@SpringBootApplication
public class AnalyticsBiodataApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnalyticsBiodataApplication.class, args);
    }

}
