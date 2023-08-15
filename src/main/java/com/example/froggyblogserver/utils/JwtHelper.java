package com.example.froggyblogserver.utils;

import java.util.Date;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtHelper {
    private static Dotenv dotenv;

    private static final String SECRET_KEY = dotenv.get("secretKey");

    private static final long EXPIRE_TIME = Long.parseLong(dotenv.get("expiredTimeAccess"));
    private static final long EXPIRE_TIME_REFRESH = Long.parseLong(dotenv.get("expiredTimeRefresh"));
    private static final String AUDIENCE = dotenv.get("audience");
    private static final String ISSUER = dotenv.get("issuer");

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
