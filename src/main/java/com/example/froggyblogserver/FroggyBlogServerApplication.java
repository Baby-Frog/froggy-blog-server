package com.example.froggyblogserver;

import com.example.froggyblogserver.common.CONSTANTS;
import com.example.froggyblogserver.entity.RoleEntity;
import com.example.froggyblogserver.repository.RoleRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.froggyblogserver.repository")
public class FroggyBlogServerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(FroggyBlogServerApplication.class, args);
    }
    @Bean 
     PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(5);
    }
    @Bean
    CommandLineRunner commandLineRunner(RoleRepo roleRepo){
        return args -> {
            var check = roleRepo.findByCode(CONSTANTS.ROLE.USER);
            if (check.isEmpty()){
                roleRepo.save(RoleEntity.builder().name("User").code(CONSTANTS.ROLE.USER).build());
            }
        };
    }
}
