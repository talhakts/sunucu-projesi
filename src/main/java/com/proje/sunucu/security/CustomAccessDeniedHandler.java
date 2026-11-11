package com.proje.sunucu.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Kontrol listesi Madde 34 ve baslangic.md kurali geregi:
 * Kimligi dogrulanmis ancak gerekli yetkisi bulunmayan isteklerde (403 Forbidden),
 * ortak JSON hata modeli formatinda yanit dondurur.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());

        String jsonResponse = String.format(
                "{\"status\": %d, \"error\": \"%s\", \"message\": \"%s\", \"timestamp\": \"%s\"}",
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                "Erisim engellendi. Bu islemi gerceklestirmek icin gerekli yetkiye sahip degilsiniz.",
                LocalDateTime.now()
        );

        response.getWriter().write(jsonResponse);
    }
}

