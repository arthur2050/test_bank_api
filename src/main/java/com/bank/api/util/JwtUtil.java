package com.bank.api.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Утилитарный класс для работы с JWT токенами.
 * <p>
 * Позволяет создавать токены, извлекать информацию и валидировать их.
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * Извлекает имя пользователя (subject) из JWT.
     *
     * @param token JWT токен
     * @return имя пользователя
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Извлекает дату истечения токена.
     *
     * @param token JWT токен
     * @return дата истечения
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Универсальный метод для извлечения любых данных из токена.
     *
     * @param token JWT токен
     * @param claimsResolver функция для извлечения данных
     * @param <T> тип данных
     * @return результат функции
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Извлекает все claims (данные) из JWT.
     *
     * @param token JWT токен
     * @return claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Проверяет, истек ли токен.
     *
     * @param token JWT токен
     * @return true если токен истек
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Валидирует токен относительно пользователя.
     *
     * @param token JWT токен
     * @param userDetails данные пользователя
     * @return true если токен действителен
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Генерирует новый JWT токен для пользователя.
     *
     * @param userDetails данные пользователя
     * @return JWT токен
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Создает JWT токен с заданными claims и subject.
     *
     * @param claims данные токена
     * @param subject имя пользователя
     * @return JWT токен
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
}
