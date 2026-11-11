Bu projenin amacı, sağlık personelinin ilaç kayıtlarını ve temel hasta gözlem notlarını
yönetebildiği, aynı zamanda sisteme yüklenen ilaç prospektüsleri üzerinden yapay zekâ
destekli soru-cevap yapabildiği sade ve güvenli bir RESTful API geliştirmektir. Dersin
kapsamına uygun olarak projede herhangi bir ön yüz (frontend) veya HTML üretimi
bulunmayacak, tüm iletişim JSON veri formatı üzerinden HTTP/REST ilkeleriyle
sağlanacaktır.
Uygulama mimarisi, Spring Boot kullanılarak Controller, Service ve Repository katmanlarının
sorumlulukları Constructor Injection ile ayrılacak şekilde tasarlanacaktır. İlişkisel veritabanı
işlemlerinde Spring Data JPA kullanılacak; Kullanıcı (User), İlaç (Medication) ve Gözlem
Notu (ObservationNote) olmak üzere temel varlıklar üzerinden CRUD işlemleri, sayfalama,
sıralama ve basit filtreleme sorguları gerçekleştirilecektir. İstemci ile veri alışverişinde Entity
sınıfları yerine DTO yapıları ve ResponseEntity kullanılacak, girdi doğrulamaları Bean
Validation ile yapılarak hatalar merkezi istisna yönetimiyle standart bir formatta
döndürülecektir. Çok adımlı basit veri kayıtlarında işlem bütünlüğü @Transactional ile
korunacaktır.
API güvenliği, durumsuz (stateless) mimaride Spring Security ve JWT kullanılarak
sağlanacaktır. Sistemde ADMIN ve DOCTOR olmak üzere iki temel rol bulunacak; kayıt
sahipliği denetimi sayesinde her doktor yalnızca kendi oluşturduğu gözlem notlarını
güncelleyebilecek veya silebilecektir.
Yapay zekâ entegrasyonunda Spring AI ve temel RAG akışı kullanılacaktır. Sisteme örnek
olarak yüklenen birkaç adet ilaç prospektüs metni parçalara ayrılarak (chunking) vektör
deposuna kaydedilecektir. Kullanıcılar REST uç noktası üzerinden ilacın yan etkileri veya
kullanım dozu hakkında soru sorduğunda, sistem benzerlik aramasıyla ilgili metin parçasını
bulacak ve ChatClient aracılığıyla kaynak destekli bir yanıt üretecektir. Bu süreçte veri
gizliliği ve hatalı üretim riskleri göz önünde bulundurulacaktır.
Son olarak, geliştirilen API uç noktaları OpenAPI ile belgelenecek, temel birim ve
entegrasyon testleri (MockMvc) yazılacak ve uygulama ortam değişkenleri ile sağlık
denetimleri yapılandırılarak Docker üzerinde çalıştırılacaktır.