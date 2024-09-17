package io.github.dougllasfps.imageliteapi.application.jwt;

import io.github.dougllasfps.imageliteapi.domain.AccessToken;
import io.github.dougllasfps.imageliteapi.domain.entity.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
//@RequiredArgsConstructor
public class JwtService {

    private final SecretKeyGenerator keyGenerator;

    public JwtService(SecretKeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    public AccessToken generateToken(User user){

        var key = KeyGenerator.getKey();
        var expirationDate = generateExpirationDate();
        var claims = generateTokenClaims(user);

        String token = Jwts
                .builder()
                .signWith(Key)
                .subject(user.getEmail())
                .expiration(expirationDate)
                .claims(claims)
                .compact();

        return new AccessToken(token);
    }

    private Date generateExpirationDate(){
        var expirationMinutos = 60;
        LocalDateTime now = LocalDateTime.now().plusMinutes(expirationMinutos);
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    }

    private Map<String, Object> generateTokenClaims(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", user.getName());
        return claims;
    }

    public String getEmailFromToken(String tokenJwt){
        try {
            Jwts.parser()
                    .verifyWith(KeyGenerator.getKey())
                    .build()
                    .parseSignedClaims(tokenJwt)
                    .getPayload()
                    .get("");
            return null;
        }catch (JwtException e){
            throw new InvalidTokenException(e.getMessage());
        }

    }

}
