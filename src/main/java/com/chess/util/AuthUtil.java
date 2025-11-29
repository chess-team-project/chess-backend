package com.chess.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 * Утилита для работы с авторизацией через заголовок Auth
 */
@Component
public class AuthUtil {

    private static final String AUTH_HEADER = "Auth";

    /**
     * Извлекает ID игрока из заголовка Auth
     */
    public String getPlayerId(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER);
        if (authHeader == null || authHeader.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing or empty Auth header");
        }
        return authHeader.trim();
    }

    /**
     * Проверяет наличие заголовка Auth
     */
    public boolean hasAuthHeader(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER);
        return authHeader != null && !authHeader.trim().isEmpty();
    }
}

