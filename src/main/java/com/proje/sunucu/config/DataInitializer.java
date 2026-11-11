package com.proje.sunucu.config;

import com.proje.sunucu.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Kontrol listesi Madde 7, 10, 12, 31 ve 32 geregi:
 * - @PostConstruct ve @PreDestroy yasam dongusu metotlarini icerir.
 * - Ozel uygulama ozelligi (@Value) ve servis bagimliliklari constructor parametresiyle alinmistir.
 * - Veritabani islemleri dogrudan repository yerine @Transactional olan Service katmani uzerinden yurutulur.
 * - Baslangicta varsayilan kullanicilarin parolalari Argon2id ile hash'lenerek kaydedilir.
 */
@Component
public class DataInitializer {

    private final String welcomeMessage;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(@Value("${app.custom.welcome-message}") String welcomeMessage,
                           UserService userService,
                           PasswordEncoder passwordEncoder) {
        this.welcomeMessage = welcomeMessage;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // Madde 10: Bean baslatilirken yurutulecek islem
    @PostConstruct
    public void init() {
        System.out.println("==================================================================");
        System.out.println(">>> [@PostConstruct] Sistem baslatiliyor...");
        System.out.println(">>> Ozel Property Degeri (@Value): " + welcomeMessage);

        // Madde 31 ve 32: Kullanicilar Service katmani (@Transactional) uzerinden Argon2id hash'li parolalarla kaydedilir
        userService.createInitialUserIfNotExists(
                "admin",
                passwordEncoder.encode("admin123"), // Argon2id hash
                "ROLE_ADMIN",
                "Sistem Yoneticisi"
        );

        userService.createInitialUserIfNotExists(
                "doktor1",
                passwordEncoder.encode("doktor123"), // Argon2id hash
                "ROLE_DOCTOR",
                "Dr. Ahmet Yilmaz"
        );

        System.out.println(">>> [@PostConstruct] Sistem baslatma ve veri hazirligi tamamlandi.");
        System.out.println("==================================================================");
    }

    // Madde 10: Bean sonlandirilirken yurutulecek temizlik islemi
    @PreDestroy
    public void cleanup() {
        System.out.println("==================================================================");
        System.out.println(">>> [@PreDestroy] Uygulama sonlandiriliyor. Kaynaklar temizlendi.");
        System.out.println("==================================================================");
    }
}
