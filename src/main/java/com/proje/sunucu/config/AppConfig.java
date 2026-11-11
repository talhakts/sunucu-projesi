package com.proje.sunucu.config;

import com.proje.sunucu.service.AuditLogService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * Kontrol listesi Madde 8 ve 9 geregi:
 * @Configuration sinifi icinde @Bean kullanilarak uygulamada kullanilan bean tanimi yapilmistir.
 * @Lazy kullanilarak bean'in ihtiyac duyulana kadar olusturulmasi ertelenmistir.
 */
@Configuration
public class AppConfig {

    @Bean
    @Lazy
    public AuditLogService auditLogService() {
        return new AuditLogService();
    }
}

