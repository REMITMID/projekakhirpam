# 📱 AKSA (Asisten Kuliah Mahasiswa) - `projekakhirpam`

Selamat datang di repositori utama proyek **AKSA (Asisten Kuliah Mahasiswa)**! Repositori ini menggunakan arsitektur **Clean Architecture** (Domain, Data, Presentation) dengan **Jetpack Compose** untuk antarmuka pengguna (UI) dan **Supabase** sebagai *Backend-as-a-Service* (BaaS).

Dokumen ini disusun sebagai panduan lengkap bagi seluruh anggota kelompok untuk melakukan *setup* lingkungan pengembangan, memahami struktur folder, dan mengimplementasikan fitur individual masing-masing.

---

## 🛠️ 1. Prasyarat Sistem

Sebelum membuka proyek di Android Studio, pastikan perangkat Anda memenuhi spesifikasi berikut:

* **Android Studio:** Ladybug / Koala (versi 2024.1.1 ke atas direkomendasikan).
* **JDK:** Java Development Kit (JDK) **17** atau yang lebih baru.
* **Android SDK:** SDK Build-Tools 35 (atau versi terbaru hingga SDK 37).
* **Akun Supabase:** Akses ke proyek Supabase kelompok.

---

## 🚀 2. Cara Clone & Setup Awal Proyek

Ikuti langkah-langkah ini saat pertama kali mengunduh proyek dari GitHub:

### Langkah 1: Clone Repositori
Buka terminal/command prompt dan jalankan:
```bash
git clone https://github.com/USERNAME/projekakhirpam.git
cd projekakhirpam
```

### Langkah 2: Buat File `local.properties`
1. Di direktori utama proyek (*root folder*), cari file `local.properties.example`.
2. Buat salinan (*copy*) file tersebut dan beri nama **`local.properties`**.
3. Buka file `local.properties` lalu isi dengan URL dan Key dari dashboard Supabase kelompok kita:

```properties
# Path SDK Android (Diisi otomatis oleh Android Studio)
sdk.dir=C\:\\Users\\USERNAME\\AppData\\Local\\Android\\Sdk

# Kredensial Supabase Proyek AKSA
SUPABASE_URL=https://your-project-id.supabase.co
SUPABASE_ANON_KEY=your-actual-anon-key-here
```

> ⚠️ **PENTING:** File `local.properties` sudah masuk ke dalam `.gitignore`. Jangan pernah melakukan `git commit` pada file yang berisi kunci rahasia Supabase!

### Langkah 3: Sync Gradle
1. Buka proyek **`projekakhirpam`** di Android Studio.
2. Tunggu proses *indexing* selesai.
3. Klik tombol **Sync Project with Gradle Files** (ikon gajah di pojok kanan atas) atau tekan `Ctrl + Shift + O` / `Cmd + Shift + O`.
4. Jalankan **Build > Rebuild Project** untuk memastikannya terkompilasi tanpa kendala.

---

## 📂 3. Struktur Folder Proyek (Clean Architecture)

Proyek ini menerapkan Clean Architecture dengan pembagian paket sebagai berikut:

```text
com.example.projekakhirpam/
├── core/
│   └── network/
│       └── SupabaseProvider.kt        <-- Inisialisasi Supabase Client (Auth & Postgrest)
│
├── domain/                            <-- [Murni Kotlin, tanpa dependensi Android/Supabase]
│   ├── model/                         <-- Entity data (User.kt, AktivitasKampus.kt, dll)
│   └── repository/                    <-- Interface/Kontrak Repositori
│
├── data/                              <-- [Logika Data & Integrasi Supabase]
│   ├── model/                         <-- DTO & Mapper JSON (@Serializable)
│   └── repository/                    <-- Implementasi Interface Repositori
│
└── presentation/                      <-- [UI & State Management]
    ├── auth/                          <-- LoginScreen, SignUpScreen, AuthViewModel
    ├── profile/                       <-- ProfileScreen
    └── [nama_fitur_kalian]/          <-- Folder fitur anggota (UI Screen & ViewModel)
```

---

## 📐 4. Panduan Pengerjaan Fitur Individual

Setiap anggota bertanggung jawab atas **1 fitur utama** yang mandiri. Mengikuti contoh dari **Fitur 6: Aktivitas Kampus** (Jack Sanders Ham), berikut tahapan pengerjaan fitur baru:

### Langkah 1: Buat Tabel di SQL Editor Supabase
Buat tabel di Supabase SQL Editor lengkap dengan kebijakan RLS (*Row Level Security*):
```sql
create table nama_tabel_kalian (
    id uuid default gen_random_uuid() primary key,
    user_id uuid references auth.users(id) on delete cascade not null,
    -- Tambahkan kolom sesuai kebutuhan fitur kalian
    created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

alter table nama_tabel_kalian enable row level security;

create policy "Akses pengguna sendiri" on nama_tabel_kalian 
for all using (auth.uid() = user_id);
```

### Langkah 2: Buat Domain Model & Interface (`domain/`)
1. Buat data class utama di `domain/model/NamaModel.kt`.
2. Buat antarmuka repositori di `domain/repository/NamaRepository.kt` dengan kontrak fungsi CRUD (`get`, `insert`, `update`, `delete`).

### Langkah 3: Buat DTO & Implementasi Repositori (`data/`)
1. Buat Data Transfer Object (DTO) di `data/model/NamaDto.kt` menggunakan anotasi `@Serializable` dan `@SerialName`.
2. Sertakan fungsi konversi `toDomain()` dan `toDto()`.
3. Buat kelas implementasi di `data/repository/NamaRepositoryImpl.kt` yang menghubungkan Supabase Postgrest ke interface domain.

### Langkah 4: Buat ViewModel & UI Screen (`presentation/`)
1. Buat folder baru di bawah `presentation/[nama_fitur]/`.
2. Buat `NamaViewModel.kt` untuk mengelola `UiState` (Loading, Success, Error).
3. Buat UI Compose untuk:
   * **Halaman List:** Wajib menggunakan `LazyColumn`, `LazyVerticalGrid`, atau `ViewPager`.
   * **Halaman Detail:** Menampilkan rincian data item.
   * **Halaman Form:** Formulir Tambah/Edit data.

### Langkah 5: Daftarkan Rute di Navigasi (`AppNavigation.kt`)
Tambahkan rute komposisi baru di file `AppNavigation.kt` agar halaman fitur kalian dapat diakses melalui navigasi utama aplikasi.

---

## 🤝 5. Alur Kerja Git & GitHub

Agar tidak terjadi bentrokan kode (*merge conflict*):

1. **Selalu Tarik Kode Terbaru Sebelum Bekerja:**
   ```bash
   git checkout main
   git pull origin main
   ```
2. **Buat Branch Fitur Baru:**
   ```bash
   git checkout -b feature/nama-fitur-kamu
   ```
3. **Commit secara Berkala:**
   ```bash
   git add .
   git commit -m "feat: menambah halaman form fitur [nama_fitur]"
   ```
4. **Push & Buat Pull Request (PR):**
   ```bash
   git push origin feature/nama-fitur-kamu
   ```

---

## ❓ Kendala & Bantuan

Jika mengalami error saat kompilasi atau sync Gradle, pastikan:
1. `SUPABASE_URL` dan `SUPABASE_ANON_KEY` di `local.properties` sudah benar.
2. Sudah melakukan **Build > Clean Project** kemudian **Rebuild Project**.
3. Jika terdapat kendala terkait arsitektur atau integrasi Supabase, tanyakan langsung di grup tim!

*Selamat mengoding dan semangat menyelesaikan proyek AKSA!* 🚀