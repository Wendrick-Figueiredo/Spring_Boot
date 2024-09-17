package io.github.dougllasfps.imageliteapi.application.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class SecretKeyGenerator {

    private SecretKey Key;

    public SecretKey getKey(){

        if(Key == null){
            Key = Jwts.SIG.HS256.key().build();
        }
        return Key;
    }
}
