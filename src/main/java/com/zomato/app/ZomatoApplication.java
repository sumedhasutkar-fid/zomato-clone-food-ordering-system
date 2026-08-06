package com.zomato.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class
ZomatoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZomatoApplication.class, args);
        System.out.println("Zomato Application is running====>>>");
    }
}
