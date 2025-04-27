# 🚀 UI Testing Framework with Cucumber & Selenium

Kerangka pengujian UI Web modern yang dibangun dengan **Cucumber**, **Selenium WebDriver**, dan **Java** untuk otomatisasi pengujian aplikasi web.

## ✨ Fitur Utama

- 🏗️ **Page Object Model (POM)** - Memisahkan logic halaman web dari kode pengujian
- 🧩 **Fluent API** - Interface yang intuitif untuk interaksi halaman
- 📝 **BDD Approach** - Penulisan pengujian dalam bahasa Gherkin yang mudah dipahami
- ⚡ **Parallel Execution** - Dukungan untuk eksekusi pengujian secara paralel
- 🌐 **Cross-browser Testing** - Dukungan untuk Chrome, Firefox, dan Edge
- 📸 **Automatic Screenshots** - Tangkapan layar otomatis saat pengujian gagal
- 🔄 **CI/CD Integration** - Integrasi dengan GitHub Actions

## 🛠️ Teknologi yang Digunakan

- Java 11+
- Gradle
- Cucumber
- Selenium WebDriver
- JUnit 5
- WebDriverManager
- SLF4J (Logging)

## 📁 Struktur Proyek

```
ui-test-cucumber/
├── src/
│   ├── main/java/com/automation/tests/
│   │   ├── driver/           # Manajemen WebDriver
│   │   ├── config/           # Konfigurasi framework
│   │   └── pages/            # Implementasi Page Object Model
│   │       └── base/         # Base page dengan metode umum
│   └── test/
│       ├── java/com/automation/tests/
│       │   ├── runner/       # Test runner Cucumber
│       │   └── steps/        # Step definitions
│       └── resources/
│           ├── features/     # File fitur Cucumber (Gherkin)
│           └── config/       # Konfigurasi pengujian
│
├── .github/workflows/        # Konfigurasi CI/CD
├── build.gradle              # Konfigurasi Gradle
└── README.md                 # Dokumentasi proyek
```

## 📋 Prasyarat

- Java 11 atau lebih tinggi
- Gradle 7.6+
- Browser: Chrome, Firefox, atau Edge

## 🚦 Cara Menjalankan Pengujian

### Menjalankan Semua Pengujian

```bash
./gradlew clean test
```

### Menjalankan dengan Browser Tertentu

```bash
./gradlew test -Dui.browser=firefox
```

### Menjalankan Tag Tertentu

```bash
./gradlew test -Dcucumber.filter.tags="@smoke"
```

## 📊 Laporan Pengujian

Setelah menjalankan pengujian, laporan tersedia di:
- HTML Report: `build/reports/cucumber.html`
- JSON Report: `build/reports/cucumber.json`

## ⚙️ Konfigurasi

Framework dapat dikonfigurasi menggunakan properties sistem atau dengan memodifikasi file `config.properties`.

### System Properties

| Property      | Deskripsi                              | Default            |
|---------------|----------------------------------------|--------------------|
| ui.browser    | Browser (chrome, firefox, edge)        | chrome             |
| ui.headless   | Mode headless                          | false              |
| ui.baseUrl    | URL dasar aplikasi                     | https://example.com|
| ui.timeout    | Timeout default dalam detik            | 10                 |

Contoh:
```bash
./gradlew test -Dui.browser=firefox -Dui.headless=true
```

## 🧩 Cara Memperluas Framework

### Menambahkan Halaman Baru

1. Buat kelas Page baru yang meng-extend BasePage
2. Tambahkan lokator WebElement menggunakan anotasi @FindBy
3. Implementasikan metode spesifik halaman

Contoh:
```java
public class ProductPage extends BasePage {
    @FindBy(id = "add-to-cart")
    private WebElement addToCartButton;
    
    public ShoppingCartPage addToCart() {
        click(addToCartButton);
        return new ShoppingCartPage();
    }
}
```

### Menambahkan Fitur Baru

1. Buat file .feature baru di direktori features
2. Tulis skenario menggunakan sintaks Gherkin
3. Implementasikan step definitions di kelas step baru atau yang sudah ada

## 🔄 Integrasi CI/CD

Framework ini menyertakan konfigurasi GitHub Actions untuk continuous integration yang akan:

1. Men-setup JDK 17
2. Menginstal browser Chrome
3. Menjalankan pengujian
4. Mengupload hasil pengujian sebagai artifacts

## 📝 Praktik Terbaik yang Diterapkan

- ✅ **Enkapsulasi** - Menyembunyikan detail implementasi internal
- ✅ **Fluent Interface** - Method chaining untuk meningkatkan keterbacaan
- ✅ **Clean Code** - Penamaan yang jelas dan struktur yang terorganisir
- ✅ **Fail-fast** - Kegagalan awal dengan validasi yang tepat
- ✅ **Central configuration** - Konfigurasi terpusat untuk kemudahan pemeliharaan
- ✅ **Logging** - Pesan log yang komprehensif untuk debug

## 📜 Lisensi

Proyek ini dilisensikan di bawah Lisensi MIT - lihat file LICENSE untuk detail.
