# Sağlık İlaç ve Hasta Gözlem Yönetim Sistemi RESTful API

Bu proje, **Sunucu Uygulama Geliştirme** dersi kapsamında geliştirilmiş, sağlık personelinin ilaç kayıtlarını ve temel hasta gözlem notlarını yönetebildiği, aynı zamanda sisteme yüklenen ilaç prospektüsleri üzerinden yapay zekâ (RAG) destekli soru-cevap yapabildiği güvenli bir RESTful API'dir.

---

## 1. Gerekli Yazılımlar

* **Java Development Kit (JDK):** Sürüm 21 (Eclipse Temurin, Oracle veya OpenJDK 21)
* **Derleme Aracı:** Apache Maven 3.9+ (veya Maven Wrapper)
* **İlişkisel Veritabanı:** MySQL 8.0+
* **Konteynerleştirme (İsteğe Bağlı):** Docker ve Docker Compose
* **API Test:** Postman veya Swagger UI

---

## 2. Veritabanı ve Bağlantı Ayarları

Uygulama çalıştırılmadan önce MySQL sunucunuzda aşağıdaki veritabanının oluşturulması gereklidir:

```sql
CREATE DATABASE IF NOT EXISTS medication_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

`src/main/resources/application.properties` içerisindeki varsayılan bağlantı ayarları:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/medication_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Kontrol listesi gereği veri yeniden başlatmada korunur:
spring.jpa.hibernate.ddl-auto=update
```

> **Not:** Kendi MySQL kullanıcı adı ve parolanız farklıysa `application.properties` dosyasındaki `spring.datasource.username` ve `spring.datasource.password` alanlarını güncelleyiniz.
> 
> **Alternatif (Ortam Değişkenleri):** Veritabanı kimlik bilgileri ortam değişkenleriyle de verilebilir (`SPRING_DATASOURCE_USERNAME` ve `SPRING_DATASOURCE_PASSWORD`). Spring Boot çalışma zamanında bu ortam değişkenlerini `application.properties` dosyasındaki değerlerin yerine kullanır.

---

## 3. Uygulamayı Başlatma Adımları

### Yöntem A: Maven / Terminal Üzerinden Başlatma
1. Proje kök dizinine gidiniz:
   ```bash
   cd <projenin-klasoru>  # pom.xml'in bulundugu klasor
   ```
2. Projeyi derleyiniz ve bağımlılıkları indiriniz:
   ```bash
   mvn clean package -DskipTests
   ```
3. Uygulamayı başlatınız:
   ```bash
   mvn spring-boot:run
   ```

### Yöntem B: IntelliJ IDEA / Eclipse / VS Code Üzerinden Başlatma
1. Projeyi Maven projesi olarak açınız.
2. `src/main/java/com/proje/sunucu/SunucuApplication.java` sınıfını bulunuz.
3. Sağ tıklayıp **Run 'SunucuApplication'** seçeneğini seçiniz.

### Yöntem C: Docker Compose ile Tek Komutla Başlatma (MySQL Dahil)
Sisteminizde Docker kuruluysa veritabanı kurmanıza gerek kalmadan tek komutla tüm sistemi ayağa kaldırabilirsiniz:
```bash
docker compose up --build
```

> **Docker Compose Notları:**
> * Docker Compose kendi MySQL konteynerini (`medication_mysql`) kurar ve bilgisayarınızda yerel olarak çalışan MySQL servisinden tamamen bağımsızdır.
> * `docker-compose.yml` dosyasında MySQL portu dışarıya (host makineye) **3307** portundan yayınlanmaktadır (`ports: - "3307:3306"`). Uygulama ise **8080** portunda çalışır.
> * Bilgisayarınızda 8080 portunu kullanan başka bir uygulama varsa Docker Compose başlatılmadan önce kapatılmalıdır.
> * İlk açılışta Docker içindeki veritabanı boştur ve `DataInitializer` bileşeni sayesinde örnek kullanıcılar ve başlangıç verileri otomatik olarak eklenir.

---

## 4. Testleri Çalıştırma

Projede yer alan MockMvc birim ve entegrasyon testlerini çalıştırmak için proje kök dizininde şu komut yürütülür:

```bash
mvn test
```

> **Veritabanı Bağlantı Gereksinimi:** Testler Spring Boot uygulama bağlamını (`@SpringBootTest`) ayağa kaldırdığı ve JPA/Hibernate EntityManager altyapısını başlattığı için, testlerin çalıştırılması sırasında `application.properties` (veya ortam değişkenleri) ile yapılandırılmış MySQL veritabanının çalışır ve erişilebilir durumda olması gerekmektedir (varsayılan: `localhost:3306/medication_db`). Veritabanı kapalıysa veya kimlik bilgileri uyuşmuyorsa test bağlamı başlatılamaz.

---

## 5. API ve Dokümantasyon Adresleri

Uygulama varsayılan olarak **8080** portunda çalışır:

* **Swagger UI (İnteraktif API Test Arayüzü):**  
  [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* **OpenAPI v3 JSON Sözleşmesi:**  
  [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
* **Actuator Sağlık Durumu (Health):**  
  [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)
* **Actuator Uygulama Bilgisi (Info):**  
  [http://localhost:8080/actuator/info](http://localhost:8080/actuator/info)

---

## 6. Değerlendirmede Kullanılacak Örnek Kullanıcılar ve Roller

Uygulama ayağa kalktığında `DataInitializer` bileşeni tarafından MySQL'e otomatik olarak eklenen kullanıcılar şunlardır (Parolalar **Argon2id** ile hash'lenmiştir):

| Kullanıcı Adı | Parola | Rol | Yetki Düzeyi |
| :--- | :--- | :--- | :--- |
| **admin** | `admin123` | `ROLE_ADMIN` | Tam Yetkili: İlaç ekleme, tam/kısmi güncelleme (PATCH), silme (DELETE), kullanıcı görüntüleme, Actuator yönetim erişimi. |
| **doktor1** | `doktor123` | `ROLE_DOCTOR` | Doktor Yetkisi: İlaçları listeleme/filtreleme, kendi gözlem notlarını oluşturma/güncelleme/silme, RAG soru-cevap. İlaç silemez (403 alır). |

> **Kimlik Doğrulama Şekli:** Güvenlik katmanı **HTTP Basic** ile çalışır. İstek başlığında `Authorization: Basic <base64(kullanıcı:parola)>` iletilmelidir.

---

## 7. Postman İstek Koleksiyonu Kullanımı

Proje kök dizininde yer alan [`postman_collection.json`](postman_collection.json) dosyası, değerlendirme için gerekli tüm uç noktaları içerir:

1. Postman uygulamasını açınız ve sol üstteki **Import** butonuna tıklayınız.
2. `postman_collection.json` dosyasını seçip içeri aktarınız.
3. Koleksiyon değişkenleri (`baseUrl`, `adminUsername`, `adminPassword`, `doctorUsername`, `doctorPassword`) önceden tanımlanmıştır.
4. Koleksiyon klasörleri:
   * **1. İlaç İşlemleri (Medication CRUD & Filter):** GET liste, GET tekil, GET filtreli/sayfalı arama, POST (201), PUT (200), PATCH (200), DELETE (204).
   * **2. Gözlem Notu İşlemleri (ObservationNote):** GET liste, POST oluşturma, PUT güncelleme, DELETE silme (Kayıt sahipliği denetimli).
   * **3. Hata Senaryoları (400, 404, 401, 403):** Geçersiz gövde (400), PATCH ile ID değiştirme teşebbüsü (400), olmayan kayıt (404), hatalı parola (401), doktorun ilaç silmesi (403).
   * **4. Yapay Zekâ (RAG Soru-Cevap):** İlaç dozu ve yan etkilerini prospektüs parçalarından alıntılayarak cevaplayan POST istekleri.
   * **5. Actuator Sağlık ve Bilgi:** Actuator endpoint'lerinin testi.

---

## 8. Kontrol Listesi Maddelerinin Karşılanma Durumu

| Madde No | Gereksinim | Karşılayan Sınıf / Konfigürasyon |
| :--- | :--- | :--- |
| **1** | Spring Boot 4.1.2, Java 21, Maven | `pom.xml` |
| **2** | Spring Data JPA / REST arayüzleri kullanılmamalı | Hiçbir `JpaRepository` kullanılmadı; JPA `EntityManager` kullanıldı. |
| **3** | Controller → Service → Repository → Veritabanı akışı | Tüm modüllerde (`Medication`, `User`, `ObservationNote`) katmanlı akış sağlandı. |
| **4** | `@RestController`, Controller'da DB işlemi yapılmaması | `MedicationController`, `UserController`, `ObservationNoteController` |
| **5** | Service arayüzleri ve `@Service` sınıfları | `MedicationService`, `UserService`, `ObservationNoteService` |
| **6** | Repository arayüzleri, `@Repository`, `EntityManager` | `MedicationRepositoryImpl`, `UserRepositoryImpl`, `ObservationNoteRepositoryImpl` |
| **7** | Yalnızca Constructor Injection | Tüm sınıflarda bağımlılıklar ve `@Value` constructor parametresiyle alındı. |
| **8** | `@Configuration` ve `@Bean` | `AppConfig`, `OpenApiConfig`, `SecurityConfig` |
| **9** | `@Lazy` bean kullanımı | `AuditLogService` (`AppConfig` içinde `@Lazy` ile tanımlandı) |
| **10** | `@PostConstruct` ve `@PreDestroy` | `DataInitializer.init()` ve `DataInitializer.cleanup()` |
| **11** | Aynı arayüzün iki implementasyonu + `@Primary` / `@Qualifier` | `NotificationService` → `EmailNotificationService` (`@Primary`) ve `SmsNotificationService` (`@Qualifier`), `NotificationManager` |
| **12** | Özel property tanımlama ve `@Value` constructor injection | `application.properties` (`app.custom.welcome-message`), `AppPropertyService`, `DataInitializer` |
| **13** | Actuator `health` ve `info` endpoint'leri | `application.properties` (`management.endpoints.web.exposure.include=health,info`) |
| **14** | Actuator endpoint'lerinin güvenliği | `SecurityConfig` (`/actuator/health` ve `/actuator/info` ADMIN/DOCTOR açık; diğer `/actuator/**` endpoint'leri ROLE_ADMIN ile korunmaktadır) |
| **15** | MySQL veritabanı ve `ddl-auto=update` | `application.properties` |
| **16** | Entity sınıflarında `@Entity`, `@Table`, `@Column` | `Medication`, `User`, `ObservationNote` |
| **17** | Parametresiz constructor, `@Id`, `@GeneratedValue(IDENTITY)` | `Medication`, `User`, `ObservationNote` |
| **18** | Ekleme, bulma, listeleme, güncelleme, silme (`EntityManager`) | `MedicationRepositoryImpl` (`persist`, `find`, `createQuery`, `merge`, `remove`) |
| **19** | Parametreli JPQL filtre sorgusu (`setParameter`) | `MedicationRepositoryImpl.findByFilter` |
| **20** | Veri değiştiren işlemlerde `@Transactional` | `MedicationServiceImpl`, `ObservationNoteServiceImpl`, `UserServiceImpl` |
| **21** | REST GET (liste ve `@PathVariable` ile kimlik) | `MedicationController.getAllMedications` ve `getMedicationById` |
| **22** | POST (istemci ID ezilmez, üretilen kimlikle sonuç) | `MedicationController.createMedication`, `MedicationServiceImpl.createMedication` |
| **23** | PUT ile tam güncelleme | `MedicationController.updateMedication`, `MedicationServiceImpl.updateMedication` |
| **24** | PATCH ile kısmi güncelleme (ID değiştirilemez) | `MedicationController.patchMedication`, `MedicationServiceImpl.patchMedication` |
| **25** | DELETE ile kimliğe göre kayıt silme | `MedicationController.deleteMedication`, `MedicationServiceImpl.deleteMedication` |
| **26** | `@RequestBody` ve Jackson dönüşümleri | Tüm Controller POST/PUT/PATCH metotları |
| **27** | Global exception handler (`@RestControllerAdvice`) | `GlobalExceptionHandler` |
| **28** | Custom exception ve ortak JSON hata modeli | `ResourceNotFoundException`, `BadRequestException`, `ErrorResponse` |
| **29** | 404 (bulunamadı) ve 400 (geçersiz istek) durum kodları | `GlobalExceptionHandler` |
| **30** | Spring Security, `SecurityFilterChain`, HTTP Basic | `SecurityConfig` |
| **31** | DB tabanlı kullanıcı, aktiflik ve roller | `User`, `CustomUserDetailsService` |
| **32** | Parolaların Argon2id ile hash'lenmesi | `Argon2PasswordEncoder`, `DataInitializer` (şifreler Argon2id ile kaydedilir) |
| **33** | Okuma/yazma/silme yetki düzeyleri (PATCH dahil) | `SecurityConfig` (GET: Read, POST/PUT/PATCH: Update, DELETE: Delete) |
| **34** | 401 ve 403 yanıtlarının ortak hata modeliyle dönmesi | `CustomAuthenticationEntryPoint` ve `CustomAccessDeniedHandler` |
| **35** | OpenAPI dokümantasyonu ve Swagger UI | `OpenApiConfig`, Controller `@Operation` ve `@Tag` anotasyonları |
| **36** | Çalıştırma rehberi | `README.md` |
| **37** | İçe aktarılabilir Postman koleksiyonu | `postman_collection.json` |
| **Ek** | Yapay Zekâ (RAG) Prospektüs Soru-Cevap | `ProspectusRagService`, `RagController` (`/api/ai/ask`) |
| **Ek** | MockMvc Birim ve Entegrasyon Testleri | `MedicationControllerTest` |
| **Ek** | Docker ve Docker Compose | `Dockerfile`, `docker-compose.yml` |

