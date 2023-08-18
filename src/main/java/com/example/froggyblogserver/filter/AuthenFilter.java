package com.example.froggyblogserver.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.exception.ValidateException;
import com.example.froggyblogserver.service.AccountService;
import com.example.froggyblogserver.utils.JwtHelper;

public class AuthenFilter extends OncePerRequestFilter {

    private JwtHelper jwtHelper;
    @Autowired
    private AccountService accountService;

    private String getTokenRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ", "");
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = getTokenRequest(request);
            if (jwt != null && jwtHelper.validateJwtToken(jwt)) {
                String username = jwtHelper.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = accountService.loadUserByUsername(username);
                if(userDetails.getAuthorities().size()<1) throw new ValidateException(MESSAGE.VALIDATE.USER_NOT_PERMISSION);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        } catch (Exception e) {
            logger.error("Can NOT set uer authentication -> Message:{}", e);
        }
        filterChain.doFilter(request, response);

    }

}
