package com.proje.sunucu.service.notification;

import org.springframework.stereotype.Service;

/**
 * Kontrol listesi Madde 11 geregi:
 * NotificationService arayuzunun ikinci implementasyonu.
 * @Qualifier("smsNotificationService") kullanilarak hedeflenerek enjekte edilebilir.
 */
@Service("smsNotificationService")
public class SmsNotificationService implements NotificationService {

    @Override
    public void sendNotification(String recipient, String message) {
        System.out.println("[SMS BILDIRIMI] Alici: " + recipient + " | Mesaj: " + message);
    }

    @Override
    public String getNotificationChannel() {
        return "SMS (Qualifier ile Secilen)";
    }
}

