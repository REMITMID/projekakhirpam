package com.example.projekakhirpam.data.repository

import com.example.projekakhirpam.domain.model.Tugas
import com.example.projekakhirpam.domain.model.TugasConst
import com.example.projekakhirpam.domain.repository.TugasRepository
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

/**
 * SAKLAR MODE DUMMY
 * true  = pakai data dummy di memori (tanpa Supabase, tanpa login)
 * false = pakai Supabase asli (TugasRepositoryImpl)
 * Ubah ke false setelah local.properties berisi URL & key Supabase asli.
 */
const val TUGAS_PAKAI_DUMMY = true

/** Repository palsu: data disimpan di memori, hilang saat aplikasi ditutup. */
class DemoTugasRepository : TugasRepository {

    private val data = mutableListOf(
        contoh("Laporan Praktikum Modul 5", "Pemrograman Aplikasi Mobile", 2,
            TugasConst.PRIORITAS_HIGH, TugasConst.STATUS_IN_PROGRESS,
            "Buat laporan praktikum tentang Navigation Compose."),
        contoh("Kuis Basis Data", "Basis Data", 0,
            TugasConst.PRIORITAS_HIGH, TugasConst.STATUS_TODO,
            "Pelajari materi normalisasi 1NF sampai 3NF."),
        contoh("Resume Jurnal", "Metodologi Penelitian", 6,
            TugasConst.PRIORITAS_MEDIUM, TugasConst.STATUS_TODO,
            "Resume 2 jurnal internasional terkait topik skripsi."),
        contoh("Desain UI Figma", "Interaksi Manusia dan Komputer", -1,
            TugasConst.PRIORITAS_MEDIUM, TugasConst.STATUS_TODO,
            "Prototype low-fidelity 5 halaman."),
        contoh("Latihan Soal Statistika", "Statistika", 10,
            TugasConst.PRIORITAS_LOW, TugasConst.STATUS_COMPLETED,
            "Kerjakan latihan bab 3.")
    )

    override suspend fun getTugasList(): Result<List<Tugas>> = runCatching {
        delay(400) // pura-pura loading seperti ambil dari internet
        data.sortedBy { it.deadline }
    }

    override suspend fun insertTugas(item: Tugas): Result<Unit> = runCatching {
        delay(300)
        data.add(item.copy(id = UUID.randomUUID().toString(), userId = "dummy-user"))
    }

    override suspend fun updateTugas(id: String, item: Tugas): Result<Unit> = runCatching {
        delay(200)
        val index = data.indexOfFirst { it.id == id }
        if (index == -1) throw Exception("tugas tidak ditemukan")
        data[index] = item.copy(id = id, userId = "dummy-user")
    }

    override suspend fun deleteTugas(id: String): Result<Unit> = runCatching {
        delay(200)
        if (!data.removeAll { it.id == id }) throw Exception("tugas tidak ditemukan")
    }

    private fun contoh(
        judul: String, mataKuliah: String, hariLagi: Int,
        prioritas: String, status: String, deskripsi: String
    ): Tugas {
        val cal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, hariLagi) }
        val tanggal = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(cal.time)
        return Tugas(
            id = UUID.randomUUID().toString(),
            userId = "dummy-user",
            judul = judul,
            mataKuliah = mataKuliah,
            deskripsi = deskripsi,
            deadline = tanggal,
            prioritas = prioritas,
            status = status
        )
    }
}
