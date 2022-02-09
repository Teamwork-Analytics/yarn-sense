package com.monash.analytics.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class AnalyticsEurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnalyticsEurekaServerApplication.class, args);
    }

}
