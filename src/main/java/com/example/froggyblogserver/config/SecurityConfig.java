package com.example.froggyblogserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.froggyblogserver.exception.AuthenEntryPoint;

import static org.springframework.security.config.Customizer.withDefaults;
import com.example.froggyblogserver.exception.AuthenAccessDeniedExceptionHandler;
import com.example.froggyblogserver.filter.AuthenFilter;
import com.example.froggyblogserver.service.AccountService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true,jsr250Enabled = true)
public class SecurityConfig {
    
    @Autowired
    private AccountService accountService;

    @Autowired 
    PasswordEncoder passwordEncoder;

    @Autowired
    public void configGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(accountService).passwordEncoder(passwordEncoder);
    }

    @Bean
    AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf -> csrf.ignoringAntMatchers("/**"));
        http.httpBasic(basic -> basic.authenticationEntryPoint(new AuthenEntryPoint()));
        http.authorizeHttpRequests(requests -> requests.antMatchers("/", "/login", "/register","/refreshToken","/api/role/**").permitAll()
                .anyRequest().authenticated()).csrf(csrf -> csrf.disable());
        http.addFilterBefore(new AuthenFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(handling -> handling.accessDeniedHandler(new AuthenAccessDeniedExceptionHandler()));
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.cors(withDefaults());
        return http.build();

    }
}