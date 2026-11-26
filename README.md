# 📱 AKSA (Asisten Kuliah Mahasiswa) - `projekakhirpam`

Selamat datang di repositori utama proyek **AKSA (Asisten Kuliah Mahasiswa)**!

AKSA merupakan aplikasi Android yang dikembangkan menggunakan **Kotlin**, **Jetpack Compose** untuk antarmuka pengguna (UI), **Clean Architecture** (Domain, Data, Presentation), dan **Supabase** sebagai *Backend-as-a-Service* (BaaS).

Dokumen ini menjadi panduan bagi seluruh anggota kelompok dalam melakukan setup proyek, memahami struktur kode, mengimplementasikan fitur masing-masing, dan bekerja secara kolaboratif menggunakan Git dan GitHub.

**Setiap anggota wajib mengerjakan fitur pada branch masing-masing yang telah disediakan.** Jangan membuat branch baru atau melakukan perubahan langsung pada `main` maupun `develop` tanpa koordinasi.

---

## 🛠️ 1. Prasyarat Sistem

Sebelum membuka proyek di Android Studio, pastikan perangkat memenuhi kebutuhan berikut:

- **Android Studio:** Koala / Ladybug (versi 2024.1.1 ke atas direkomendasikan).
- **JDK:** Java Development Kit (JDK) 17 atau versi yang kompatibel dengan konfigurasi Gradle proyek.
- **Android SDK:** SDK dan Build-Tools sesuai konfigurasi proyek.
- **Akun Supabase:** Memiliki akses ke proyek Supabase kelompok.
- **Git:** Sudah terinstal untuk mengelola branch dan kolaborasi.

---

## 🚀 2. Cara Clone & Setup Awal Proyek

Langkah ini dilakukan ketika pertama kali mengunduh proyek.

### Langkah 1: Clone Repositori

Buka terminal atau Git Bash, kemudian jalankan:

```bash
git clone https://github.com/REMITMID/projekakhirpam.git
cd projekakhirpam
```

### Langkah 2: Pilih Branch Sesuai Fitur

Seluruh branch fitur telah tersedia di GitHub, sehingga **tidak perlu membuat branch baru**.

Ambil informasi branch terbaru:

```bash
git fetch origin
```

Lihat semua branch lokal dan remote:

```bash
git branch -a
```

Kemudian pindah ke branch sesuai fitur masing-masing.

Contoh untuk fitur Catatan Akademik:

```bash
git switch feature/catatan-akademik
```

Jika branch belum tersedia di komputer lokal:

```bash
git switch --track origin/feature/catatan-akademik
```

Ganti nama branch sesuai pembagian fitur anggota.

Pastikan branch sudah benar:

```bash
git branch --show-current
```

**Penting:** Semua proses pengembangan berikutnya dilakukan pada branch fitur masing-masing.

### Langkah 3: Buat File `local.properties`

1. Di direktori utama proyek (*root folder*), cari file `local.properties.example`.
2. Salin file tersebut dan beri nama `local.properties`.
3. Jika file contoh tidak tersedia, buat `local.properties` sesuai konfigurasi yang dibutuhkan proyek.
4. Masukkan URL dan key Supabase yang telah dibagikan kepada anggota.

Contoh:

```properties
# Path SDK Android (biasanya diisi Android Studio)
sdk.dir=C\:\\Users\\USERNAME\\AppData\\Local\\Android\\Sdk

# Konfigurasi Supabase AKSA
SUPABASE_URL=https://your-project-id.supabase.co
SUPABASE_ANON_KEY=your-publishable-or-anon-key
```

> **Penting:** Pastikan `local.properties` tercantum dalam `.gitignore`. Jangan mengunggah file konfigurasi lokal, password, ataupun Supabase `service_role`/secret key ke GitHub. Key publik/anon Supabase digunakan bersama kebijakan RLS yang benar, bukan sebagai pengganti pengamanan database.

### Langkah 4: Sync Gradle

1. Buka folder `projekakhirpam` di Android Studio.
2. Tunggu proses *indexing* selesai.
3. Klik **Sync Project with Gradle Files**.
4. Jalankan **Build > Rebuild Project** jika tersedia.
5. Jalankan aplikasi pada emulator atau perangkat Android.

Pastikan proyek dapat dijalankan sebelum mulai mengembangkan fitur.

---

## 📂 3. Struktur Folder Proyek (Clean Architecture)

Proyek menerapkan Clean Architecture dengan pembagian lapisan Domain, Data, dan Presentation.

```text
com.example.projekakhirpam/
│
├── core/
│   └── network/
│       └── SupabaseProvider.kt
│
├── domain/
│   ├── model/
│   └── repository/
│
├── data/
│   ├── model/
│   └── repository/
│
└── presentation/
    ├── auth/
    ├── profile/
    └── [nama_fitur_kalian]/
```

**Penjelasan struktur:**

| Folder | Tanggung Jawab |
|---|---|
| `core/network/` | Konfigurasi dan inisialisasi Supabase Client. |
| `domain/model/` | Model atau entitas utama aplikasi. |
| `domain/repository/` | Interface atau kontrak akses data. |
| `data/model/` | DTO dan mapper data dari Supabase. |
| `data/repository/` | Implementasi repository dan operasi database. |
| `presentation/auth/` | Antarmuka serta state autentikasi. |
| `presentation/profile/` | Antarmuka profil pengguna. |
| `presentation/[nama_fitur]/` | Screen, ViewModel, dan komponen UI milik fitur masing-masing. |

Alur dependensi yang digunakan:

```text
UI (Jetpack Compose)
        |
        v
     ViewModel
        |
        v
Domain Repository Interface
        ^
        |
Data Repository Implementation
        |
        v
Supabase Client
        |
        v
Supabase Database
```

Domain tidak boleh bergantung langsung pada komponen Android, UI, maupun Supabase.

---

## 📐 4. Panduan Pengerjaan Fitur Individual

Setiap anggota bertanggung jawab atas satu fitur utama sesuai pembagian tugas dan branch yang telah disediakan.

Sebagai contoh, berikut tahapan pengembangan **Fitur 6: Aktivitas Kampus**.

### Langkah 1: Buat Tabel di Supabase

Buat tabel sesuai kebutuhan fitur melalui SQL Editor Supabase.

Contoh:

```sql
create table nama_tabel_kalian (
    id uuid default gen_random_uuid() primary key,
    user_id uuid references auth.users(id)
        on delete cascade not null,
    -- Tambahkan kolom sesuai kebutuhan fitur
    created_at timestamptz default now() not null
);

alter table nama_tabel_kalian
enable row level security;

create policy "Pengguna dapat membaca data sendiri"
on nama_tabel_kalian
for select
using ((select auth.uid()) = user_id);

create policy "Pengguna dapat menambah data sendiri"
on nama_tabel_kalian
for insert
with check ((select auth.uid()) = user_id);

create policy "Pengguna dapat mengubah data sendiri"
on nama_tabel_kalian
for update
using ((select auth.uid()) = user_id)
with check ((select auth.uid()) = user_id);

create policy "Pengguna dapat menghapus data sendiri"
on nama_tabel_kalian
for delete
using ((select auth.uid()) = user_id);
```

Gunakan nama tabel yang berbeda sesuai fitur masing-masing. Koordinasikan perubahan skema database agar tidak mengganggu fitur anggota lain.

### Langkah 2: Buat Domain Model & Interface (`domain/`)

1. Buat model di `domain/model/NamaModel.kt`.
2. Buat kontrak repository di `domain/repository/NamaRepository.kt`.
3. Definisikan operasi sesuai kebutuhan fitur, seperti `get`, `insert`, `update`, dan `delete`.

### Langkah 3: Buat DTO & Repository (`data/`)

1. Buat DTO di `data/model/NamaDto.kt` menggunakan `@Serializable` dan `@SerialName` sesuai kebutuhan.
2. Buat mapper seperti `toDomain()` dan `toDto()`.
3. Buat implementasi di `data/repository/NamaRepositoryImpl.kt`.
4. Hubungkan repository dengan Supabase Postgrest.

### Langkah 4: Buat ViewModel & UI (`presentation/`)

Buat folder sesuai nama fitur.

Contoh:

```text
presentation/
└── aktivitas_kampus/
    ├── AktivitasKampusViewModel.kt
    ├── AktivitasKampusListScreen.kt
    ├── AktivitasKampusDetailScreen.kt
    └── AktivitasKampusFormScreen.kt
```

Setiap fitur wajib memiliki:

- **Halaman List:** Menampilkan daftar menggunakan `LazyColumn`, `LazyVerticalGrid`, atau komponen daftar Compose yang sesuai.
- **Halaman Detail:** Menampilkan informasi lengkap suatu item.
- **Halaman Form:** Menambahkan atau mengedit data.
- **ViewModel:** Mengelola state UI, seperti Loading, Success, dan Error.
- **CRUD Supabase:** Menyimpan dan mengambil data sesuai pengguna yang login.

### Langkah 5: Integrasi Navigasi

Daftarkan halaman fitur pada sistem navigasi aplikasi melalui `AppNavigation.kt` atau file navigasi yang digunakan proyek.

Karena navigasi merupakan bagian bersama, **koordinasikan perubahan pada file tersebut dengan anggota yang bertanggung jawab atas integrasi aplikasi**.

---

## 🤝 5. Alur Kerja Git & GitHub

Proyek AKSA menggunakan sistem kerja berbasis branch agar setiap anggota dapat mengembangkan fitur secara terpisah tanpa mengganggu pekerjaan anggota lain.

### 5.1 Daftar Branch yang Sudah Disediakan

Berikut branch yang digunakan dalam proyek:

| Branch | Kegunaan |
|---|---|
| [`main`](https://github.com/REMITMID/projekakhirpam/tree/main) | Versi utama aplikasi yang telah stabil. |
| [`develop`](https://github.com/REMITMID/projekakhirpam/tree/develop) | Integrasi dan pengujian seluruh fitur. |
| [`auth`](https://github.com/REMITMID/projekakhirpam/tree/auth) | Login, registrasi, dan autentikasi. |
| [`feature/manajemen-tugas-kuliah`](https://github.com/REMITMID/projekakhirpam/tree/feature/manajemen-tugas-kuliah) | Manajemen Tugas Kuliah. |
| [`feature/jadwal-perkuliahan`](https://github.com/REMITMID/projekakhirpam/tree/feature/jadwal-perkuliahan) | Jadwal Perkuliahan. |
| [`feature/catatan-akademik`](https://github.com/REMITMID/projekakhirpam/tree/feature/catatan-akademik) | Catatan Akademik. |
| [`feature/direktori-dosen`](https://github.com/REMITMID/projekakhirpam/tree/feature/direktori-dosen) | Direktori Dosen. |
| [`feature/koleksi-referensi`](https://github.com/REMITMID/projekakhirpam/tree/feature/koleksi-referensi) | Koleksi Referensi. |
| [`feature/aktivitas-kampus`](https://github.com/REMITMID/projekakhirpam/tree/feature/aktivitas-kampus) | Aktivitas Kampus. |

**Aturan utama:**

Setiap anggota hanya mengerjakan fitur pada branch yang telah ditentukan. Jangan membuat branch baru karena branch fitur sudah tersedia di repositori GitHub.

### 5.2 Cara Mulai Bekerja pada Branch Masing-Masing

Setelah melakukan clone, masuk ke branch fitur sesuai tugas.

Contoh untuk Catatan Akademik:

```bash
git fetch origin
git switch feature/catatan-akademik
```

Jika Git tidak menemukan branch lokal, gunakan:

```bash
git switch --track origin/feature/catatan-akademik
```

Pastikan branch yang aktif:

```bash
git branch --show-current
```

Output yang diharapkan:

```text
feature/catatan-akademik
```

Untuk fitur lain, cukup ganti nama branch sesuai pembagian tugas.

### 5.3 Alur Pengerjaan Setiap Hari

Setiap kali akan melanjutkan pengembangan, lakukan langkah berikut.

**1. Pastikan branch yang aktif sudah benar**

```bash
git branch --show-current
```

**2. Ambil perubahan terbaru dari branch masing-masing**

Contoh Catatan Akademik:

```bash
git pull --ff-only origin feature/catatan-akademik
```

**3. Kerjakan fitur sesuai tanggung jawab**

Implementasikan Domain, Data, ViewModel, UI, dan integrasi Supabase tanpa mengubah fitur anggota lain.

**4. Periksa perubahan kode**

```bash
git status
```

**5. Commit hasil pekerjaan**

```bash
git add .
git commit -m "feat: menambahkan halaman catatan akademik"
```

**6. Push ke branch masing-masing**

```bash
git push origin feature/catatan-akademik
```

Semua contoh perintah di atas menggunakan branch Catatan Akademik. Anggota lain harus menggantinya sesuai branch sendiri.

Jika terdapat perubahan yang belum di-commit, simpan atau selesaikan terlebih dahulu sebelum melakukan pull maupun berpindah branch.

### 5.4 Aturan Kolaborasi

Agar tidak terjadi *merge conflict*, seluruh anggota wajib mengikuti aturan berikut:

1. Jangan mengerjakan atau melakukan push langsung ke `main` dan `develop`.
2. Gunakan branch fitur yang sudah disediakan.
3. Hindari mengubah file milik fitur anggota lain.
4. Koordinasikan perubahan pada file bersama, seperti `AppNavigation.kt`, `SupabaseProvider.kt`, konfigurasi Gradle, dan komponen UI global.
5. Jangan menghapus atau mengganti kode anggota lain tanpa koordinasi.
6. Lakukan commit secara berkala dengan pesan yang jelas.
7. Pastikan fitur dapat di-build dan diuji sebelum membuat Pull Request.
8. Jangan menggunakan `git push --force` pada branch bersama tanpa persetujuan tim.

### 5.5 Penggabungan Fitur ke Branch `develop`

Setelah implementasi fitur selesai dan sudah diuji, anggota membuat **Pull Request (PR)** ke `develop`.

Tahapannya:

1. Commit dan push seluruh perubahan ke branch fitur masing-masing.
2. Buka repositori GitHub AKSA.
3. Pilih **Compare & pull request** atau buat Pull Request baru.
4. Tentukan `base: develop` dan `compare: feature/nama-fitur`.
5. Jelaskan perubahan dan pengujian yang telah dilakukan.
6. Koordinator atau anggota yang ditunjuk melakukan review.
7. Setelah disetujui, perubahan digabungkan ke `develop`.

Jangan langsung menggabungkan fitur ke `main`.

### 5.6 Alur Integrasi Keseluruhan

```text
       BRANCH FITUR MASING-MASING
                    |
                    v
          Implementasi Fitur
                    |
                    v
             Build & Testing
                    |
                    v
              Commit & Push
                    |
                    v
              Pull Request
                    |
                    v
             Review oleh Tim
                    |
                    v
                 develop
                    |
                    v
         Integrasi Seluruh Fitur
                    |
                    v
          Pengujian Aplikasi
                    |
                    v
                  main
```

`develop` menjadi tempat integrasi seluruh fitur, sedangkan `main` menyimpan versi aplikasi yang telah stabil.

Jika terjadi konflik penggabungan kode, selesaikan bersama anggota yang terlibat sebelum Pull Request digabungkan.

---

## ❓ 6. Kendala & Bantuan

Jika mengalami masalah selama pengembangan, lakukan pemeriksaan berikut:

1. Pastikan `SUPABASE_URL` dan `SUPABASE_ANON_KEY` pada `local.properties` sudah benar.
2. Pastikan Gradle Sync berhasil.
3. Jalankan **Build > Clean Project** dan **Rebuild Project** jika diperlukan.
4. Periksa apakah branch yang digunakan sudah benar melalui `git branch --show-current`.
5. Jika terjadi masalah Git, periksa kondisi repository dengan `git status`.
6. Jika terdapat kendala pada arsitektur, konfigurasi bersama, database Supabase, atau integrasi fitur, diskusikan di grup tim sebelum mengubah kode bersama.

**Selamat mengoding dan semangat menyelesaikan proyek AKSA!** 🚀
