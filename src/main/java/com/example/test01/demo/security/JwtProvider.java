package com.example.test01.demo.security;


import com.example.test01.demo.entity.UserIn;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtProvider {
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    @Value("${app.jwtRefreshSecret}")
    private String jwtRefreshSecret;

    @Value("${app.jwtRefreshExpirationInMs}")
    private int jwtRefreshExpirationInMs;

    private String generateToken(final UserIn user, final String secret, final int expiration,final SignatureAlgorithm algorithm){
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime expire = now.plusSeconds(expiration);
        return Jwts.builder()
                .setSubject(Long.toString(user.getId()))
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expire.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(algorithm,secret)
                .compact();
    }

    public String generateAuthToken(final UserIn user){
        return generateToken(user,jwtSecret,jwtExpirationInMs,SignatureAlgorithm.HS512);
    }

    public String generateRefresh(final UserIn user){
        return generateToken(user,jwtRefreshSecret,jwtRefreshExpirationInMs,SignatureAlgorithm.HS512);
    }

    private Long getUserIdFromJWT(final String token,final String secret){
        final Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public Long getUserIdFromAuthJWT(final String token) {
        return getUserIdFromJWT(token,jwtSecret);
    }

    public Long getUserIdFromRefreshJWT(final String token) {
        return getUserIdFromJWT(token,jwtRefreshSecret);
    }

    private boolean validate(final String Token, final String signature){
        try {
            Jwts.parser().setSigningKey(signature).parseClaimsJws(Token);
            return true;
        } catch (SignatureException ex) {
            System.out.println("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty.");
        }
        return false;
    }

    public boolean ValidateRefreshToken(final String authToken){
        return validate(authToken,jwtRefreshSecret);
    }

    public boolean validateToken(final String authToken) {
        return validate(authToken,jwtSecret);
    }

}
