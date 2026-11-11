package com.proje.sunucu.service.notification;

/**
 * Kontrol listesi Madde 11 geregi:
 * Ayni arayuzun birden fazla bean implementasyonu bulunacaktir.
 */
public interface NotificationService {

    void sendNotification(String recipient, String message);

    String getNotificationChannel();
}

