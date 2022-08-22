package com.smart;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

    public static void main(String [] args){
        // 主要用于路由和过滤
        SpringApplication.run(GatewayApplication.class,args);
    }
}
