package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EWMService {
    public static void main(String[] args) {
        System.out.println("main in play");
        SpringApplication.run(EWMService.class, args);
    }
}
