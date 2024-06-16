package org.delivery.api.domain.token.helper;

import io.jsonwebtoken.*;
import org.delivery.api.common.error.TokenErrorCode;
import org.delivery.api.common.exception.ApiException;
import org.delivery.api.domain.token.ifs.TokenHelperIfs;
import org.delivery.api.domain.token.model.TokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenHelper implements TokenHelperIfs {

    @Value("${token.secret.key}")
    private String secretKey;

    @Value("{token.access-token.plus-hour}")
    private Long accessTokenPlusHour;

    @Value("{token.refresh-token.plus-hour}")
    private Long refreshTokenPlusHour;

    @Override
    public TokenDto issueAccessToken(Map<String, Object> data) {
        LocalDateTime expiredLocalDateTime = LocalDateTime.now().plusHours(accessTokenPlusHour);
        Date expiredAt = Date.from(expiredLocalDateTime.atZone(
                        ZoneId.systemDefault())
                                .toInstant());

        SecretKey key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());

        var jwtToken = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setClaims(data)
                .setExpiration(expiredAt)
                .compact();

        return TokenDto.builder()
                .token(jwtToken)
                .expiredAt(expiredLocalDateTime)
                .build();
    }

    @Override
    public TokenDto issueRefreshToken(Map<String, Object> data) {
        LocalDateTime expiredLocalDateTime = LocalDateTime.now().plusHours(refreshTokenPlusHour);
        Date expiredAt = Date.from(expiredLocalDateTime.atZone(
                        ZoneId.systemDefault())
                .toInstant());

        SecretKey key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());

        var jwtToken = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setClaims(data)
                .setExpiration(expiredAt)
                .compact();

        return TokenDto.builder()
                .token(jwtToken)
                .expiredAt(expiredLocalDateTime)
                .build();
    }
    @Override
    public Map<String, Object> validationTokenWithThrow(String token) {

        SecretKey key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS256.getJcaName());

        var parser = Jwts.parser()
                .setSigningKey(key)
                .build();

        try{
            Jwe<Claims> result = parser.parseEncryptedClaims(token); // 복호화
            return new HashMap<>(result.getBody());
        }catch(Exception e){
            if(e instanceof SignatureException){
                throw new ApiException(TokenErrorCode.INVALID_TOKEN, e);
            }else if(e instanceof ExpiredJwtException){
                throw new ApiException(TokenErrorCode.EXPIRED_TOKEN, e);
            }else{
                throw new ApiException(TokenErrorCode.EXCEPTION_TOKEN, e);
            }
        }
    }
}
