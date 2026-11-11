package com.proje.sunucu.config;

import com.proje.sunucu.entity.User;
import com.proje.sunucu.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Kontrol listesi Madde 7, 10, 12, 31 ve 32 geregi:
 * - @PostConstruct ve @PreDestroy yasam dongusu metotlarini icerir.
 * - Ozel uygulama ozelligi (@Value) ve PasswordEncoder constructor parametresiyle alinmistir.
 * - Baslangicta varsayilan kullanicilarin parolalari Argon2id ile hash'lenerek MySQL'e kaydedilir.
 */
@Component
public class DataInitializer {

    private final String welcomeMessage;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(@Value("${app.custom.welcome-message}") String welcomeMessage,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.welcomeMessage = welcomeMessage;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Madde 10: Bean baslatilirken yurutulecek islem
    @PostConstruct
    @Transactional
    public void init() {
        System.out.println("==================================================================");
        System.out.println(">>> [@PostConstruct] Sistem baslatiliyor...");
        System.out.println(">>> Ozel Property Degeri (@Value): " + welcomeMessage);

        // Madde 31 ve 32: Kullanicilar Argon2id hash'li parolalarla kaydedilir
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User(
                    null,
                    "admin",
                    passwordEncoder.encode("admin123"), // Argon2id hash
                    "ROLE_ADMIN",
                    true,
                    "Sistem Yoneticisi"
            );
            userRepository.save(admin);
            System.out.println(">>> [@PostConstruct] Varsayilan ADMIN kullanicisi (Argon2id ile) olusturuldu: admin");
        }

        if (userRepository.findByUsername("doktor1").isEmpty()) {
            User doctor = new User(
                    null,
                    "doktor1",
                    passwordEncoder.encode("doktor123"), // Argon2id hash
                    "ROLE_DOCTOR",
                    true,
                    "Dr. Ahmet Yilmaz"
            );
            userRepository.save(doctor);
            System.out.println(">>> [@PostConstruct] Varsayilan DOCTOR kullanicisi (Argon2id ile) olusturuldu: doktor1");
        }

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
