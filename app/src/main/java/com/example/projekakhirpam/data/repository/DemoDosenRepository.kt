package com.example.projekakhirpam.data.repository

import com.example.projekakhirpam.domain.model.Dosen
import com.example.projekakhirpam.domain.repository.DosenRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

/**
 * In-Memory Demo Repository untuk Direktori Dosen.
 * Digunakan untuk presentasi UI & demonstrasi fitur CRUD interaktif.
 */
class DemoDosenRepository : DosenRepository {

    private val _dosenList = MutableStateFlow(
        listOf(
            Dosen(
                id = "dosen-1",
                nama = "Muhammad Aminul Akbar, S.Kom., M.T.",
                fotoUrl = null,
                noWa = "",
                email = "muhammad.aminul@ub.ac.id",
                mataKuliah = "Pemrograman Aplikasi Mobile",
                kontakPreferensi = "Email",
                karakter = listOf("Sabar", "Baik"),
                catatan = "Saat proyek kelompok cenderung pengerjaan secara individual"
            ),
            Dosen(
                id = "dosen-2",
                nama = "Dr. Eng. Herman Tolle, S.T., M.T.",
                fotoUrl = null,
                noWa = "081298765432",
                email = "herman.tolle@ub.ac.id",
                mataKuliah = "Interaksi Manusia & Komputer",
                kontakPreferensi = "Telegram",
                karakter = listOf("Rinci & terstruktur", "Fokus UX & Desain", "Disiplin waktu"),
                catatan = "Sering aktif di grup Telegram kelas. Kirim pesan dengan format sopan: salam, nama, NIM, dan kelas. Utamakan konsep User Experience."
            ),
            Dosen(
                id = "dosen-3",
                nama = "Wayan Firdaus Mahmudy, S.Si., M.T., Ph.D.",
                fotoUrl = null,
                noWa = "081333444555",
                email = "wayanfm@ub.ac.id",
                mataKuliah = "Kecerdasan Buatan",
                kontakPreferensi = "Tatap Muka",
                karakter = listOf("Logika kuat", "Solutif", "Diskusi terbuka"),
                catatan = "Janji temu bimbingan dilakukan minimal H-1 di ruangan dosen. Bawa draf proposal/ide yang sudah dipelajari sebelum konsultasi."
            ),
            Dosen(
                id = "dosen-4",
                nama = "Tri Astoto Kurniawan, S.T., M.T., Ph.D.",
                fotoUrl = null,
                noWa = "081555666777",
                email = "triastoto@ub.ac.id",
                mataKuliah = "Rekayasa Perangkat Lunak",
                kontakPreferensi = "Email",
                karakter = listOf("Standar industri", "UML teliti", "Ramah mahasiswa"),
                catatan = "Boleh kirim draf dokumen SDD/SRS via email dengan subjek yang jelas. Selalu gunakan penomoran versi dokumen yang rapi."
            )
        )
    )

    override fun getDosenList(): Flow<List<Dosen>> = _dosenList.asStateFlow()

    override suspend fun getDosenById(id: String): Dosen? {
        delay(150)
        return _dosenList.value.find { it.id == id }
    }

    override suspend fun insertDosen(dosen: Dosen): Result<Unit> {
        delay(300)
        val newId = if (dosen.id.isBlank()) UUID.randomUUID().toString() else dosen.id
        val newDosen = dosen.copy(id = newId)
        _dosenList.value = _dosenList.value + newDosen
        return Result.success(Unit)
    }

    override suspend fun updateDosen(id: String, dosen: Dosen): Result<Unit> {
        delay(300)
        val currentList = _dosenList.value
        val index = currentList.indexOfFirst { it.id == id }
        if (index != -1) {
            _dosenList.value = currentList.map { if (it.id == id) dosen.copy(id = id) else it }
            Result.success(Unit)
        } else {
            Result.failure(Exception("Dosen dengan ID $id tidak ditemukan"))
        }
        return Result.success(Unit)
    }

    override suspend fun deleteDosen(id: String): Result<Unit> {
        delay(200)
        _dosenList.value = _dosenList.value.filterNot { it.id == id }
        return Result.success(Unit)
    }
}

