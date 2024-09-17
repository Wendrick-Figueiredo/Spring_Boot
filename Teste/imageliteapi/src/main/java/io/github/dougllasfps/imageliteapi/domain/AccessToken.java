package io.github.dougllasfps.imageliteapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessToken {
    private String accessToken;
    private Long expiration;
    AccessToken autheticate(String email, String password);
}
