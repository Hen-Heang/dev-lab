package com.learn.jwt;

import com.learn.security.CustomUserDetail;
import io.jsonwebtoken.Claims;

import javax.crypto.SecretKey;

public interface JwtService {

    Claims extractClaims(String token);
    SecretKey getKey();
    String generateToken(CustomUserDetail customUserDetail);
    String refreshToken(CustomUserDetail customUserDetail);
    boolean isValidToken(String token);
}
