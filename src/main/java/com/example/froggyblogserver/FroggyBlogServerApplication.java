package com.example.froggyblogserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class FroggyBlogServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FroggyBlogServerApplication.class, args);
    }

     @Bean
    public Dotenv dotenv() {
        return Dotenv.configure().load();
    }

    @Bean 
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(17);
    }
}
