package com.chess.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Фильтр для проверки заголовка Authorization со значением 'sfhasdjf'
 * Применяется ко всем запросам, кроме /api/health
 */
@Component
public class AuthFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String REQUIRED_AUTH_VALUE = "sfhasdjf";

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) 
            throws ServletException, IOException {
        
        String requestPath = request.getRequestURI();
        
        // Пропускаем health check без проверки авторизации
        if (requestPath.equals("/api/health")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Проверяем заголовок Authorization
        String authHeader = request.getHeader(AUTH_HEADER);
        
        if (authHeader == null || !authHeader.trim().equals(REQUIRED_AUTH_VALUE)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Unauthorized: Missing or invalid Authorization header\"}");
            return;
        }
        
        // Если заголовок корректен, пропускаем запрос дальше
        filterChain.doFilter(request, response);
    }
}

