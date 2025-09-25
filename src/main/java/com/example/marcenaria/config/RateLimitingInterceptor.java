package com.example.marcenaria.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingInterceptor implements HandlerInterceptor {
    
    private static class RequestInfo {
        private int requestCount;
        private LocalDateTime lastRequest;
        
        public RequestInfo() {
            this.requestCount = 1;
            this.lastRequest = LocalDateTime.now();
        }
        
        public boolean isAllowed(int maxRequests, int windowMinutes) {
            LocalDateTime now = LocalDateTime.now();
            long minutesElapsed = ChronoUnit.MINUTES.between(lastRequest, now);
            
            if (minutesElapsed >= windowMinutes) {
                // Reset window
                requestCount = 1;
                lastRequest = now;
                return true;
            } else if (requestCount < maxRequests) {
                requestCount++;
                return true;
            } else {
                return false; // Rate limit exceeded
            }
        }
    }
    
    private final ConcurrentHashMap<String, RequestInfo> requestTracker = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS = 60; // 60 requests
    private static final int WINDOW_MINUTES = 1; // per minute
    
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, 
                           @NonNull HttpServletResponse response, 
                           @NonNull Object handler) throws Exception {
        
        // Aplicar rate limiting apenas para API de orçamento
        if (!request.getRequestURI().startsWith("/api/orcamento")) {
            return true;
        }
        
        String ip = getClientIpAddress(request);
        RequestInfo requestInfo = requestTracker.computeIfAbsent(ip, k -> new RequestInfo());
        
        if (requestInfo.isAllowed(MAX_REQUESTS, WINDOW_MINUTES)) {
            return true;
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(
                "{\"error\":\"Muitas tentativas. Aguarde um minuto antes de tentar novamente.\"}");
            return false;
        }
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}