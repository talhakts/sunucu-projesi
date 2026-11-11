package com.proje.sunucu.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Kontrol listesi Madde 7, 14, 30, 32, 33, 34 geregi:
 * Spring Security ve SecurityFilterChain yapilandirmasi.
 * - HTTP Basic kimlik dogrulamasi kullanilir (JWT kullanilmaz).
 * - Parolalar Argon2id ile hash'lenir (Argon2PasswordEncoder).
 * - Okuma, ekleme/guncelleme (PATCH dahil) ve silme islemleri icin ayri yetki duzeyleri belirlenmistir.
 * - Actuator ucnoktalari korunmaktadir.
 * - 401 ve 403 yanitlari ozel JSON hata modeli dondurur.
 * - Constructor injection uygulanmistir.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(CustomAuthenticationEntryPoint authenticationEntryPoint,
                          CustomAccessDeniedHandler accessDeniedHandler) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    // Madde 32: Parolalarin Argon2id ile hash'lenmesini saglayan PasswordEncoder bean'i
    @Bean
    public PasswordEncoder passwordEncoder() {
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // 1. OpenAPI ve Swagger UI ucnoktalarina herkes erisebilir
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                // 2. Madde 14: Actuator yonetim ucnoktalarinin korunmasi
                .requestMatchers("/actuator/health", "/actuator/info").hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers("/actuator/**").hasRole("ADMIN")

                // 3. Madde 33: Okuma islemleri (GET) - Hem ADMIN hem DOCTOR erisebilir
                .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("ADMIN", "DOCTOR")

                // 4. Madde 33: Gozlem notu yazma/guncelleme/silme ve AI RAG soru-cevap ucnoktasi
                .requestMatchers(HttpMethod.POST, "/api/notes/**").hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers(HttpMethod.PUT, "/api/notes/**").hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers(HttpMethod.DELETE, "/api/notes/**").hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers(HttpMethod.POST, "/api/ai/**").hasAnyRole("ADMIN", "DOCTOR")

                // 5. Madde 33: Ilac ekleme, tam guncelleme ve kismi guncelleme (PATCH guncelleme yetkisi ister)
                .requestMatchers(HttpMethod.POST, "/api/medications/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/medications/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/medications/**").hasRole("ADMIN")

                // 6. Madde 33: Silme islemleri (DELETE) - Yalnizca en yuksek yetki olan ADMIN gerceklestirebilir
                .requestMatchers(HttpMethod.DELETE, "/api/medications/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")

                // Diger tum istekler kimlik dogrulamasi gerektirir
                .anyRequest().authenticated()
            )
            // Madde 30: HTTP Basic kimlik dogrulamasi
            .httpBasic(basic -> basic.authenticationEntryPoint(authenticationEntryPoint))
            // Madde 34: 401 ve 403 durumlarinda standart JSON hata modeli dondurme
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
            );

        return http.build();
    }
}
