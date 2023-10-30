package com.example.froggyblogserver.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

@Configuration
public class MailConfig {
    @Value("${mail.port}")
    private int PORT ;
    @Value("${mail.host}")
    private String HOST ;
    @Value("${mail.username}")
    private String USERNAME ;
    @Value("${mail.password}")
    private String PASSWORD ;
    private final String[] LANGUAGE = {"en","vi"};

    @Bean
    public JavaMailSender getJavaMailSender() throws MessagingException {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(HOST);
        mailSender.setPort(PORT);

        mailSender.setUsername(USERNAME);
        mailSender.setPassword(PASSWORD);
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.debug", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", HOST);
        properties.put("mail.smtp.port", PORT);
        return mailSender;
    }

    @Bean
    MimeMessage mimeMessage() throws MessagingException {
        MimeMessage mimeMessage = getJavaMailSender().createMimeMessage();
        mimeMessage.setFrom(USERNAME);
        mimeMessage.setContentLanguage(LANGUAGE);
        return mimeMessage;
    }
}
