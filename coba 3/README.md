# SpringStore

SpringStore adalah aplikasi e-commerce backend sederhana berbasis **Spring Boot** yang mendukung fitur-fitur seperti autentikasi JWT, manajemen produk, keranjang belanja, dan pemesanan.

## 🚀 Fitur Utama

- Autentikasi dan otorisasi menggunakan JWT
- CRUD produk dan komentar
- Manajemen keranjang belanja
- Pemrosesan pesanan (order)
- Penanganan error global
- Pemetaan DTO & entity dengan MapStruct
- Validasi input
- Konfigurasi keamanan dengan Spring Security

## 🛠️ Teknologi

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- JWT
- H2 / MySQL
- Maven

## 🧑‍💻 Cara Menjalankan Lokal

```bash
# Clone repositori
git clone https://github.com/username/springstore.git
cd springstore

# Bangun project
./mvnw clean install

# Jalankan aplikasi
./mvnw spring-boot:run
```

Aplikasi akan tersedia di: [http://localhost:8080](http://localhost:8080)

## 🔑 Konfigurasi Environment

Buat file `.env` atau konfigurasi di `application.properties`:

```
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/springstore
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=yourpassword
JWT_SECRET=your_jwt_secret
```

## 🚢 Deployment ke Railway

### 1. Build aplikasi
```bash
./mvnw clean package -DskipTests
```

### 2. Pastikan file berikut tersedia:

#### `Procfile`
```
web: java -jar target/springstore-0.0.1-SNAPSHOT.jar
```

#### `.env.example`
```env
SPRING_DATASOURCE_URL=jdbc:mysql://your-host:3306/your-db
SPRING_DATASOURCE_USERNAME=your-db-user
SPRING_DATASOURCE_PASSWORD=your-db-password
JWT_SECRET=your-jwt-secret
```

### 3. Deploy ke Railway
- Login ke Railway
- Buat project baru (Spring Boot / Java)
- Upload ke GitHub dan hubungkan ke Railway
- Railway akan otomatis build dan menjalankan `Procfile`

## 🧪 Testing

CI/CD pipeline via GitHub Actions tersedia di `.github/workflows/test.yml`.

---

© 2025 SpringStore. Dibuat untuk pembelajaran dan pengembangan backend.