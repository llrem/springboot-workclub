package com.yu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yu.mapper")
public class WorkclubApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkclubApplication.class, args);
    }

}
