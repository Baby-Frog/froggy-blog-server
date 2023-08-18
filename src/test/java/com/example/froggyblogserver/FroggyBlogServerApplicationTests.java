package com.example.froggyblogserver;

import java.security.SecureRandom;
import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

@SpringBootTest
class FroggyBlogServerApplicationTests {

    // @Test
    // void contextLoads() {
    // int numKeys = 3;
    // int keyLength = 32;

    // String[] secretKeys = new String[numKeys];
    // SecureRandom random = new SecureRandom();

    // for (int i = 0; i < numKeys; i++) {
    // byte[] keyBytes = new byte[keyLength];
    // random.nextBytes(keyBytes);
    // String secretKey =
    // Base64.getUrlEncoder().withoutPadding().encodeToString(keyBytes);
    // secretKeys[i] = secretKey;
    // }

    // String secretKeyString = String.join(".", secretKeys);
    // System.err.println(secretKeyString);
    // }
    // @Value("${jwt.issuer}")
    // private  String ISSUER;
        
    @Test
    void testpropertis(){
        
    }
}
