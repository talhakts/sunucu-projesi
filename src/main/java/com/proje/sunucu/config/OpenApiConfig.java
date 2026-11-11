package com.proje.sunucu.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Kontrol listesi Madde 8 ve 35 geregi:
 * OpenAPI 3 ve Swagger UI yapilandirmasi.
 * HTTP Basic kimlik dogrulama semasi Swagger UI uzerine eklenmistir.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "basicAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Saglik Ilac ve Hasta Gozlem Yonetim Sistemi API")
                        .version("1.0.0")
                        .description("Sunucu Uygulama Gelistirme dersi kapsaminda gelistirilen RESTful API dokumantasyonu. " +
                                     "Tüm CRUD operasyonlari, sayfalama/filtreleme ve HTTP Basic guvenlik katmani belgelenmistir.")
                        .contact(new Contact()
                                .name("Proje Gelistiricisi")
                                .email("ogrenci@universite.edu.tr")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")
                                        .description("HTTP Basic Kimlik Dogrulama (Ornekler: admin / admin123 veya doktor1 / doktor123)")));
    }
}

