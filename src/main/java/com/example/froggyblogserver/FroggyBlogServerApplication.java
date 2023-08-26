package com.example.froggyblogserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.froggyblogserver.repository")
public class FroggyBlogServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FroggyBlogServerApplication.class, args);
    }


    @Bean 
     PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(17);
    }
}
