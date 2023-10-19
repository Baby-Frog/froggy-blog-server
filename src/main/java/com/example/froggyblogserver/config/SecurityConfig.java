package com.example.froggyblogserver.config;

import com.example.froggyblogserver.sercurity.handler.OAuthSuccessHandler;
import com.example.froggyblogserver.service.impl.CustomOAuth2UserService;
import com.example.froggyblogserver.utils.JwtHelper;
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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.HttpCookie;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    @Autowired
    private AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtHelper jwtHelper;
    @Autowired
    CustomOAuth2UserService customOAuth2UserService;
    @Autowired
    OAuthSuccessHandler successHandler;

    @Bean
    WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .maxAge(3600)
                        .allowCredentials(false);
            }
        };
    }

    @Autowired
    public void configGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService).passwordEncoder(passwordEncoder);
    }

    @Bean
    AuthenFilter authenFilter() {
        return AuthenFilter.builder().jwtHelper(jwtHelper).accountService(accountService).build();
    }

    @Bean
    AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.headers()
                .contentSecurityPolicy("default-src 'self';")
                .and()
                .httpStrictTransportSecurity()
                    .maxAgeInSeconds(31536000)
                    .includeSubDomains(true);

        http.authorizeHttpRequests()
                .antMatchers(
                         "/api/user/findById/**"
                        ,"/api/user/search"
                        ,"/api/comment/search/**"
                        ,"/api/comment/count/**"
                        ,"/api/like/count/**"
                        ,"/api/post/findById/**"
                        ,"/api/post/findPostByUserId/**"
                        ,"/api/post/trending"
                        ,"/api/image/get/**"
                        ,"/api/captcha/**"
                        ,"/login"
                        ,"/api/logout"
                        ,"/register"
                        ,"/refreshToken"
                        ,"/forgotPassword"
                        ,"/resetPassword"
                        ,"/api/topic/search/**"
                        ,"/api/post/search/**")
                    .permitAll()
                .anyRequest().authenticated()
                .and()
                    .oauth2Login()
                        .userInfoEndpoint()
                        .userService(customOAuth2UserService)
                .and()
                    .successHandler(successHandler);
        http.csrf().ignoringAntMatchers("/**");
        http.csrf().disable();
        http.cors(withDefaults());
        http.addFilterBefore(authenFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                    .authenticationEntryPoint(new AuthenEntryPoint())
                    .accessDeniedHandler(new AuthenAccessDeniedExceptionHandler());
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();

    }
}
