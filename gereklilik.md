Sunucu Uygulama Geliştirme - Proje Teknik Gereklilikler
Kontrol Listesi
Spring Boot 4.1.2 kullanılmalıdır.
Spring Data JPA ve Spring Data REST kullanılmamalıdır. JpaRepository, CrudRepository
gibi hazır Spring Data repository arayüzlerinden veya otomatik REST endpoint üretiminden
yararlanılmamalıdır.
Controller, Service ve Repository katmanları açıkça gerçeklenmelidir. İstekler Controller
→ Service → Repository → Veritabanı akışını izlemelidir.
Controller sınıfları @RestController ile tanımlanmalı ve HTTP istek/cevaplarını yönetmelidir.
Controller katmanında doğrudan veritabanı işlemi yapılmamalıdır.
Service arayüzleri ve bunları gerçekleyen @Service sınıfları bulunmalıdır. İş kuralları ve işlem
akışları bu katmanda yürütülmelidir.
Repository arayüzleri ve veri erişim metotlarını açıkça gerçekleyen @Repository sınıfları
bulunmalıdır. Veritabanı işlemleri JPA/Hibernate ve EntityManager üzerinden
gerçekleştirilmelidir.
Dependency injection yalnızca constructor üzerinden yapılmalıdır. Setter injection ve
field injection kullanılmamalıdır. Service, Repository ve diğer bean bağımlılıkları constructor
parametreleriyle alınmalıdır.
En az bir @Configuration sınıfında @Bean kullanılarak uygulamada kullanılan bir bean
tanımlanmalıdır.
En az bir bean için @Lazy kullanılarak ihtiyaç duyulana kadar oluşturulmasının ertelenmesi
sağlanmalıdır.
Bean başlatılırken yürütülecek bir işlem için @PostConstruct, bean sonlandırılırken yürütülecek
bir temizlik işlemi için @PreDestroy kullanılmalıdır.
Aynı arayüzün birden fazla bean implementasyonu bulunmalı; enjekte edilecek implementasyon
@Qualifier ve/veya @Primary ile belirlenmelidir.
En az bir özel uygulama özelliği (property) tanımlanmalı ve uygulamada kullanılmalıdır. Özellik
değeri @Value ile constructor parametresine alınmalıdır.
Spring Boot Actuator üzerinden uygulama sağlığına ilişkin health ve uygulama adı, açıklaması
ve sürümünü içeren info endpoint'leri sunulmalıdır.
1 / 3
Yönetim bilgisi sunan Actuator endpoint'leri uygun erişim kurallarıyla korunmalıdır.
Uygulama verileri MySQL veritabanında kalıcı olarak saklanmalı ve uygulama yeniden
başlatıldığında korunmalıdır.
Entity sınıflarında @Entity ve @Table, kalıcı alanların sütun eşlemelerinde @Column açıkça
kullanılmalıdır.
Entity sınıflarında public veya protected parametresiz constructor bulunmalıdır. Birincil
anahtarlar @Id ile tanımlanmalı; otomatik kimlik üretimi için veritabanına uygun @GeneratedValue
stratejisi kullanılmalıdır.
Repository katmanında ekleme, kimlikle bulma, listeleme, güncelleme ve silme metotları
gerçeklenmelidir. Bu işlemlerde uygun EntityManager metotları kullanılmalıdır.
Kimlikle aramaya ek olarak en az bir alan üzerinden filtreleme yapan parametreli JPQL sorgusu
bulunmalıdır. Sorgu parametreleri setParameter ile bağlanmalıdır.
Veriyi değiştiren işlemlerin transaction sınırları Service katmanında
org.springframework.transaction.annotation.Transactional ile tanımlanmalıdır.
REST endpoint'leri kaynak odaklı adlandırılmalı; işlemler uygun HTTP metotlarıyla sunulmalıdır.
GET ile hem kayıt listesi hem de kimliğe göre tek kayıt alınabilmelidir. URL'deki kimlik
@PathVariable ile karşılanmalıdır.
POST ile JSON gövdesinden yeni kayıt oluşturulmalı ve üretilen kimlikle birlikte sonuç
döndürülmelidir. İstemcinin gönderdiği kimlik mevcut bir kaydın yanlışlıkla güncellenmesine
neden olmamalıdır.
PUT ile mevcut bir kaydın tam güncellemesi yapılabilmelidir.
PATCH ile mevcut bir kaydın kısmi güncellemesi yapılabilmelidir. Yalnızca gönderilen alanlar
değişmeli, gönderilmeyen alanların değerleri korunmalıdır. PATCH gövdesiyle birincil anahtarın
değiştirilmesi engellenmelidir.
DELETE ile kimliğe göre kayıt silinebilmelidir.
2 / 3
Veri içeren istek gövdeleri @RequestBody ile alınmalı; JSON-Java dönüşümleri Jackson
üzerinden gerçekleştirilmelidir.
Global exception handler ve özel hata mesajları (custom error message) kullanılmalıdır.
Hatalar @RestControllerAdvice veya @ControllerAdvice ile tanımlanan merkezi bir sınıfta
@ExceptionHandler metotlarıyla yönetilmelidir.
Uygulamaya özgü hata durumları için custom exception sınıfları ve durum kodu, açıklayıcı özel
hata mesajı ve zaman bilgisi içeren ortak bir JSON hata modeli bulunmalıdır.
Bulunamayan kayıtlar için 404, geçersiz istekler için 400 döndürülmelidir. HTTP durum kodları ve
hata gövdeleri birbiriyle tutarlı olmalıdır.
Spring Security ve SecurityFilterChain kullanılarak HTTP Basic kimlik doğrulaması
yapılandırılmalıdır.
Kullanıcılar, aktiflik bilgileri ve roller veritabanında tutulmalı; kimlik doğrulama veritabanındaki
kullanıcı bilgileri üzerinden yapılmalıdır.
Parolalar Argon2id ile hash'lenerek saklanmalıdır. Kimlik doğrulamada Argon2id ile uyumlu
bir PasswordEncoder kullanılmalı ve parola alanı üretilen hash değerinin tamamını
saklayabilmelidir.
Okuma, ekleme/güncelleme ve silme işlemleri için farklı yetki düzeyleri tanımlanmalıdır.
Yetkilendirme HTTP metodu ve endpoint yolu üzerinden uygulanmalı; PATCH işlemi de
güncelleme yetkisi gerektirmelidir.
Kimlik doğrulaması bulunmayan veya başarısız olan korumalı istekler için 401, kimliği
doğrulanmış fakat gerekli yetkisi olmayan istekler için 403 döndürülmelidir.
OpenAPI dokümantasyonu ve Swagger UI bulunmalıdır. API'nin GET, POST, PUT, PATCH
ve DELETE işlemleri ile istek/cevap modelleri belgelenmeli; Swagger UI ve OpenAPI çıktısı
erişilebilir olmalıdır.
Bir çalıştırma rehberi hazırlanmalıdır. Rehber; gerekli yazılımları, veritabanı ve bağlantı
ayarlarını, uygulamayı başlatma adımlarını, API/Swagger adreslerini ve değerlendirmede
kullanılacak örnek kullanıcıların giriş bilgileri ile rollerini içermelidir.
İçe aktarılabilir bir Postman istek koleksiyonu hazırlanmalıdır. Koleksiyon; listeleme, tek
kayıt okuma, ekleme, tam güncelleme, kısmi güncelleme, silme ve 400, 404, 401, 403
durumlarına yönelik istekleri içermelidir. Base URL ve kimlik doğrulama bilgileri değişkenlerle
yönetilmelidir.
3 / 3