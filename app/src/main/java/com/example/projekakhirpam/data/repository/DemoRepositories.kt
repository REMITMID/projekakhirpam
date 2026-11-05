package com.example.projekakhirpam.data.repository

import com.example.projekakhirpam.domain.model.AktivitasKampus
import com.example.projekakhirpam.domain.model.CatatanAkademik
import com.example.projekakhirpam.domain.model.MataKuliahCatatan
import com.example.projekakhirpam.domain.model.PertemuanCatatan
import com.example.projekakhirpam.domain.model.Referensi
import com.example.projekakhirpam.domain.model.User
import com.example.projekakhirpam.domain.repository.AktivitasRepository
import com.example.projekakhirpam.domain.repository.AuthRepository
import com.example.projekakhirpam.domain.repository.CatatanRepository
import com.example.projekakhirpam.domain.repository.ReferensiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.util.UUID

/** Repository lokal untuk preview UI. Data akan kembali ke contoh awal saat aplikasi dimulai ulang. */
class DemoAuthRepository : AuthRepository {
    private var signedInUser: User? = User("demo-user", "demo@aksa.local")

    override suspend fun login(email: String, pass: String): Result<User> =
        Result.success(User("demo-user", email.ifBlank { "demo@aksa.local" }).also { signedInUser = it })

    override suspend fun signUp(email: String, pass: String): Result<User> = login(email, pass)
    override suspend fun getCurrentUser(): User? = signedInUser
    override suspend fun logout(): Result<Unit> {
        signedInUser = null
        return Result.success(Unit)
    }
}

class DemoAktivitasRepository : AktivitasRepository {
    private val items = mutableListOf(
        AktivitasKampus("demo-activity-1", "demo-user", "Seminar Teknologi AI", "Seminar", "2026-11-15", "Aula Kampus", "BEM", "Seminar pengenalan kecerdasan buatan.", "Akan diikuti"),
        AktivitasKampus("demo-activity-2", "demo-user", "Bakti Sosial Mahasiswa", "Sosial", "2026-11-20", "Desa Sukamaju", "Komunitas Relawan", "Kegiatan pengabdian masyarakat.", "Terdaftar"),
        AktivitasKampus("demo-activity-3", "demo-user", "Workshop UI/UX", "Workshop", "2026-11-25", "Lab Komputer", "Himpunan Mahasiswa", "Belajar dasar desain antarmuka.", "Tertarik")
    )

    override suspend fun getAktivitasList() = Result.success(items.toList())
    override suspend fun insertAktivitas(item: AktivitasKampus): Result<Unit> {
        items.add(item.copy(id = UUID.randomUUID().toString(), userId = "demo-user"))
        return Result.success(Unit)
    }
    override suspend fun updateAktivitas(id: String, item: AktivitasKampus): Result<Unit> {
        val index = items.indexOfFirst { it.id == id }
        if (index >= 0) items[index] = item.copy(id = id, userId = "demo-user")
        return Result.success(Unit)
    }
    override suspend fun deleteAktivitas(id: String): Result<Unit> {
        items.removeAll { it.id == id }
        return Result.success(Unit)
    }
}

class DemoReferensiRepository : ReferensiRepository {
    private val items = MutableStateFlow(
        listOf(
            Referensi(1, "demo-user", "Pembelajaran Mobile dengan Jetpack Compose", "Andi Pratama", "2025", "Buku", "https://example.com/compose", "Referensi dasar untuk pengembangan aplikasi Android."),
            Referensi(2, "demo-user", "Penerapan Supabase pada Aplikasi Akademik", "Siti Rahma", "2024", "Jurnal", "https://example.com/supabase", "Studi integrasi backend untuk aplikasi mahasiswa."),
            Referensi(3, "demo-user", "Panduan Desain Antarmuka Mobile", "Budi Santoso", "2023", "Web", "https://example.com/ui-mobile", "Kumpulan prinsip desain UI mobile.")
        )
    )

    override suspend fun getSemuaReferensi(): Flow<List<Referensi>> = items.asStateFlow()
    override suspend fun getReferensiById(id: Int): Referensi? = items.value.find { it.id == id }
    override suspend fun tambahReferensi(referensi: Referensi) {
        val nextId = (items.value.maxOfOrNull { it.id } ?: 0) + 1
        items.value = items.value + referensi.copy(id = nextId, userId = "demo-user")
    }
    override suspend fun updateReferensi(referensi: Referensi) {
        items.value = items.value.map { if (it.id == referensi.id) referensi else it }
    }
    override suspend fun hapusReferensi(id: Int) {
        items.value = items.value.filterNot { it.id == id }
    }
}

class DemoCatatanRepository : CatatanRepository {
    private val mataKuliah = mutableListOf(
        MataKuliahCatatan("demo-course-1", 1, "Pemrograman Mobile"),
        MataKuliahCatatan("demo-course-2", 1, "Basis Data"),
        MataKuliahCatatan("demo-course-3", 2, "Interaksi Manusia dan Komputer")
    )
    private val pertemuan = mutableListOf(
        PertemuanCatatan("demo-meeting-1", "demo-course-1", 1),
        PertemuanCatatan("demo-meeting-2", "demo-course-1", 2),
        PertemuanCatatan("demo-meeting-3", "demo-course-2", 1)
    )
    private val catatan = mutableListOf(
        CatatanAkademik("demo-note-1", "demo-meeting-1", "Pengenalan Compose", "Compose membangun UI Android secara deklaratif."),
        CatatanAkademik("demo-note-2", "demo-meeting-1", "State di Compose", "Gunakan state agar tampilan mengikuti perubahan data."),
        CatatanAkademik("demo-note-3", "demo-meeting-3", "Dasar Relasi", "Relasi tabel mencakup one-to-one, one-to-many, dan many-to-many.")
    )

    override suspend fun mataKuliah(semester: Int) = mataKuliah.filter { it.semester == semester }
    override suspend fun tambahMataKuliah(semester: Int, nama: String) {
        mataKuliah.add(MataKuliahCatatan(UUID.randomUUID().toString(), semester, nama.trim()))
    }
    override suspend fun hapusMataKuliah(id: String) {
        mataKuliah.removeAll { it.id == id }
        val meetingIds = pertemuan.filter { it.mataKuliahId == id }.map { it.id }.toSet()
        pertemuan.removeAll { it.mataKuliahId == id }
        catatan.removeAll { it.pertemuanId in meetingIds }
    }
    override suspend fun pertemuan(mataKuliahId: String) = pertemuan.filter { it.mataKuliahId == mataKuliahId }
    override suspend fun tambahPertemuan(mataKuliahId: String, nomor: Int) {
        pertemuan.add(PertemuanCatatan(UUID.randomUUID().toString(), mataKuliahId, nomor))
    }
    override suspend fun hapusPertemuan(id: String) {
        pertemuan.removeAll { it.id == id }
        catatan.removeAll { it.pertemuanId == id }
    }
    override suspend fun catatan(pertemuanId: String) = catatan.filter { it.pertemuanId == pertemuanId }
    override suspend fun simpanCatatan(pertemuanId: String, id: String?, judul: String, isi: String) {
        if (id == null) {
            catatan.add(CatatanAkademik(UUID.randomUUID().toString(), pertemuanId, judul.trim(), isi.trim()))
        } else {
            val index = catatan.indexOfFirst { it.id == id }
            if (index >= 0) catatan[index] = catatan[index].copy(judul = judul.trim(), isi = isi.trim())
        }
    }
    override suspend fun hapusCatatan(id: String) {
        catatan.removeAll { it.id == id }
    }
}
