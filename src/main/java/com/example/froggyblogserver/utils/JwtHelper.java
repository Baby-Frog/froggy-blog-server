package com.example.froggyblogserver.utils;

import java.util.Date;

import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.exception.AuthenExeption;
import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JwtHelper {

    @Value("${jwt.secretKey}")
    private String SECRET_KEY ;
    @Value("${jwt.refreshKey}")
    private String REFRESH_KEY;
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
                .signWith(SignatureAlgorithm.HS512,REFRESH_KEY)
                .compact();
    }

    public boolean validateJwtToken(String authToken){
        try{
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
            return true;
        }catch (SignatureException e){
            log.error("Invalid JWT signature -> Message: {}",e.getMessage());
            throw new AuthenExeption(MESSAGE.TOKEN.SIGNATURE_INVALID);
        }catch (MalformedJwtException e){
            log.error("Invalid JWT token -> Message: {}",e.getMessage());
            throw new AuthenExeption(MESSAGE.TOKEN.TOKEN_INVALID);
        }catch (ExpiredJwtException e){
            log.error("Expired JWT token -> Message: {}",e.getMessage());
            throw new AuthenExeption(MESSAGE.TOKEN.TOKEN_EXPIRES);
        }catch (UnsupportedJwtException e){
            log.error("Unsupported JWT token -> Message: {}",e.getMessage());
            throw new AuthenExeption(MESSAGE.TOKEN.TOKEN_UNSUPORTED);
        }catch (IllegalArgumentException e){
            log.error("JWT claim string is empty -> Message:{}", e.getMessage());
            throw new AuthenExeption(MESSAGE.TOKEN.TOKEN_INVALID);
        }

    }

    public boolean validateRefreshToken(String refreshToken){
        try{
            Jwts.parser().setSigningKey(REFRESH_KEY).parseClaimsJws(refreshToken);
            return true;
        }catch (SignatureException e){
            log.error("Invalid JWT signature -> Message: {}",e.getMessage());
            throw new AuthenExeption(MESSAGE.TOKEN.SIGNATURE_INVALID);
        }catch (MalformedJwtException e){
            log.error("Invalid JWT token -> Message: {}",e.getMessage());
            throw new AuthenExeption(MESSAGE.TOKEN.TOKEN_INVALID);
        }catch (ExpiredJwtException e){
            log.error("Expired JWT token -> Message: {}",e.getMessage());
            throw new AuthenExeption(MESSAGE.TOKEN.TOKEN_EXPIRES);
        }catch (UnsupportedJwtException e){
            log.error("Unsupported JWT token -> Message: {}",e.getMessage());
            throw new AuthenExeption(MESSAGE.TOKEN.TOKEN_UNSUPORTED);
        }catch (IllegalArgumentException e){
            log.error("JWT claim string is empty -> Message:{}", e.getMessage());
            throw new AuthenExeption(MESSAGE.TOKEN.TOKEN_INVALID);
        }

    }

    public String getUserNameFromJwtToken(String token){
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public String getUserNameFromRefreshToken(String token){
        return Jwts.parser()
                .setSigningKey(REFRESH_KEY)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }
}
