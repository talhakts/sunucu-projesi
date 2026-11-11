package com.proje.sunucu.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Kontrol listesi Madde 12 ve 7 geregi:
 * Ozel uygulama ozelligini (property) constructor parametresi uzerinden @Value ile alan bilesen.
 * Field injection veya setter injection kesinlikle kullanilmamistir.
 */
@Component
public class AppPropertyService {

    private final String welcomeMessage;

    public AppPropertyService(@Value("${app.custom.welcome-message}") String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }
}

