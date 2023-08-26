package com.example.froggyblogserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.froggyblogserver.controller.AuthenController;
@WebMvcTest(AuthenController.class)
// @SpringBootTest
public class AuthenTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    public void testRegister() throws Exception{
        String body = "{\"username\": \"viet\",\"password\": \"123\",\"rePassword\": \"123\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/register").content(body).contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("Expected Response"));
            
    }
}
