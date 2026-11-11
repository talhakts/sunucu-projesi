package com.proje.sunucu.service;

/**
 * Kontrol listesi Madde 8 ve 9 geregi:
 * AppConfig icinde @Bean ve @Lazy olarak tanimlanan,
 * yalnizca cagrildiginda belleğe yuklenen denetim (audit) log servisi.
 */
public class AuditLogService {

    public AuditLogService() {
        System.out.println(">>> [LAZY BEAN] AuditLogService baslatildi - @Lazy sayesinde yalnizca ihtiyac duyuldugunda olusturuldu.");
    }

    public void logAction(String action, String username) {
        System.out.println("[DENETIM LOGU] Eylem: " + action + " | Kullanici: " + username);
    }
}

