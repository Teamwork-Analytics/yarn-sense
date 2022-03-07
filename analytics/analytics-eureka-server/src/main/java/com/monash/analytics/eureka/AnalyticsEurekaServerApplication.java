package com.monash.analytics.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


/**
 * main Eureka server application start point
 *
 * This module only handles the Eureka server
 *
 * @author Xinyu Li
 */

@EnableEurekaServer
@SpringBootApplication
public class AnalyticsEurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnalyticsEurekaServerApplication.class, args);
    }

}
