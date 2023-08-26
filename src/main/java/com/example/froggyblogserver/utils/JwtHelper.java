package com.example.froggyblogserver.utils;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtHelper {

    @Value("${jwt.secretKey}")
    private String SECRET_KEY ;

    @Value("${jwt.expiredTimeAccess}")
    private long EXPIRE_TIME;
    @Value("${jwt.expiredTimeRefresh}")
    private long EXPIRE_TIME_REFRESH ;
    @Value("${jwt.audience}")
    private String AUDIENCE ;
    @Value("${jwt.issuer}")
    private String ISSUER ;

    public String generateAccessToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setAudience(AUDIENCE)
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS512,SECRET_KEY)
                .compact();
    }
    public String generateRefreshToken(String username){
        return Jwts.builder().setSubject(username)
                .setAudience(AUDIENCE)
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + EXPIRE_TIME_REFRESH))
                .signWith(SignatureAlgorithm.HS512,SECRET_KEY)
                .compact();
    }

    public boolean validateJwtToken(String authToken){
        try{
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
            return true;
        }catch (SignatureException e){
            log.error("Invalid JWT signature -> Message: {}",e);
        }catch (MalformedJwtException e){
            log.error("Invalid JWT token -> Message: {}",e);
        }catch (ExpiredJwtException e){
            log.error("Expired JWT token -> Message: {}",e);
        }catch (UnsupportedJwtException e){
            log.error("Unsupported JWT token -> Message: {}",e);
        }catch (IllegalArgumentException e){
            log.error("JWT claim string is empty -> Message:{}", e);
        }
        return false;

    }

    public String getUserNameFromJwtToken(String token){
        String userName = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody().getSubject();
        return userName;
    }
}
