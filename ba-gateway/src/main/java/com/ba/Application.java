package com.ba;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient(autoRegister = false) // 禁用 Nacos客户端
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
