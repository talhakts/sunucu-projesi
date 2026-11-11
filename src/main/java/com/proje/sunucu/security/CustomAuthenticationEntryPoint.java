package com.proje.sunucu.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Kontrol listesi Madde 34 ve baslangic.md kurali geregi:
 * Kimlik dogrulamasi bulunmayan veya gecersiz olan isteklerde (401 Unauthorized),
 * ortak JSON hata modeli formatinda yanit dondurur.
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        String jsonResponse = String.format(
                "{\"status\": %d, \"error\": \"%s\", \"message\": \"%s\", \"timestamp\": \"%s\"}",
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "Kimlik dogrulamasi basarisiz veya eksik. Lutfen gecerli HTTP Basic kullanici adi ve parola giriniz.",
                LocalDateTime.now()
        );

        response.getWriter().write(jsonResponse);
    }
}

