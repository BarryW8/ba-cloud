package com.ba;


import com.ba.interceptor.FeignClientInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.ba.mapper")
@EnableFeignClients
@EnableDiscoveryClient
public class BaSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaSystemApplication.class, args);
    }

    @Bean
    public FeignClientInterceptor getFeignClientInterceptor(){
        return new FeignClientInterceptor();
    }

}
