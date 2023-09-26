package com.example.froggyblogserver.exception;

import java.io.IOException;
import java.io.PrintWriter;

import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.utils.JwtHelper;
import com.example.froggyblogserver.utils.StringHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthenEntryPoint implements AuthenticationEntryPoint {

    JwtHelper jwtHelper = new JwtHelper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.error(authException.getMessage());
        try {
            String authHeader = request.getHeader("Authorization");
            if (StringHelper.isNullOrEmpty(authHeader))
                throw new AuthenExeption(MESSAGE.TOKEN.TOKEN_INVALID);
            jwtHelper.validateJwtToken(authHeader.replace("Bearer ", ""));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            writer.print(mapper.writeValueAsString(BaseResponse.builder().message(e.getMessage()).statusCode(401).build()));
            writer.flush();
        }
    }

}
