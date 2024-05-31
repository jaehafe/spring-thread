package com.spring.board.service;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private static final SecretKey key = Jwts.SIG.HS256.key().build();

    private String generateToken(String subject) {
        var now = new Date();
        var exp = new Date(now.getTime() + (1000 * 60 * 60 * 3));

        return Jwts.builder().subject(subject).signWith(key)
                .issuedAt(now)
                .expiration(exp)
                .compact();
    }
}
