package com.proje.sunucu.service.notification;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Kontrol listesi Madde 7 ve 11 geregi:
 * Ayni arayuzun iki implementasyonunun (@Primary ve @Qualifier)
 * Constructor Injection ile enjekte edilisini gosteren yonetici bilesen.
 */
@Component
public class NotificationManager {

    private final NotificationService defaultService;
    private final NotificationService smsService;

    public NotificationManager(NotificationService defaultService,
                               @Qualifier("smsNotificationService") NotificationService smsService) {
        this.defaultService = defaultService;
        this.smsService = smsService;
    }

    public void notifyViaDefault(String recipient, String message) {
        defaultService.sendNotification(recipient, message);
    }

    public void notifyViaSms(String recipient, String message) {
        smsService.sendNotification(recipient, message);
    }

    public String getDefaultChannelName() {
        return defaultService.getNotificationChannel();
    }

    public String getSmsChannelName() {
        return smsService.getNotificationChannel();
    }
}

