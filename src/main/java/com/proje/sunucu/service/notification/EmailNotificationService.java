package com.proje.sunucu.service.notification;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Kontrol listesi Madde 11 geregi:
 * NotificationService arayuzunun birinci implementasyonu.
 * @Primary olarak isaretlenmistir; varsayilan olarak bu bean enjekte edilir.
 */
@Service("emailNotificationService")
@Primary
public class EmailNotificationService implements NotificationService {

    @Override
    public void sendNotification(String recipient, String message) {
        System.out.println("[E-POSTA BILDIRIMI] Alici: " + recipient + " | Mesaj: " + message);
    }

    @Override
    public String getNotificationChannel() {
        return "EMAIL (Primary)";
    }
}

