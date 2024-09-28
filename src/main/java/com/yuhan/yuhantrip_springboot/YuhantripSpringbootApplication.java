package com.yuhan.yuhantrip_springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"PlaceDB","com.yuhan.yuhantrip_springboot.domain"})
public class YuhantripSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(YuhantripSpringbootApplication.class, args);
    }

}
