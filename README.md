# Helidon 4 Workshop

Minimal bir Helidon SE 4 uygulaması. Java sınıfları sorumluluklarına göre `application`,
`controller`, `service` ve `model` paketlerine ayrılmıştır. Helidon 4 WebServer gelen
istekleri Java virtual thread'leri üzerinde çalıştırır.

Uygulama portu, karşılama metni ve saat dilimi
`src/main/resources/application.yaml` dosyasından okunur.

## Gereksinimler

- Java 21
- Maven 3.6.3+

## Çalıştırma

```bash
mvn clean package
java -jar target/helidon4-workshop.jar
```

Başka bir terminalden:

```bash
curl http://localhost:8080/api/greetings/Emre
curl http://localhost:8080/api/time
curl http://localhost:8080/api/calculations/add/12.5/7.5
curl http://localhost:8080/api/calculations/multiply/6/7
```

Örnek yanıt:

```json
{
  "message": "Merhaba, Emre!",
  "threadName": "...",
  "virtualThread": true
}
```

Testleri çalıştırmak için:

```bash
mvn test
```
