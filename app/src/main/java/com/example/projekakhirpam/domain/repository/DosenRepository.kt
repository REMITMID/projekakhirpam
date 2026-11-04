package com.example.projekakhirpam.domain.repository

import com.example.projekakhirpam.domain.model.Dosen
import kotlinx.coroutines.flow.Flow

/**
 * Kontrak interface repositori untuk fitur Direktori Dosen.
 * Mengikuti prinsip Clean Architecture murni.
 */
interface DosenRepository {
    /**
     * Mengalirkan daftar seluruh dosen yang tersimpan secara reaktif.
     */
    fun getDosenList(): Flow<List<Dosen>>

    /**
     * Mengambil detail dosen berdasarkan ID.
     */
    suspend fun getDosenById(id: String): Dosen?

    /**
     * Menambahkan data dosen baru.
     */
    suspend fun insertDosen(dosen: Dosen): Result<Unit>

    /**
     * Memperbarui data dosen yang sudah ada.
     */
    suspend fun updateDosen(id: String, dosen: Dosen): Result<Unit>

    /**
     * Menghapus data dosen berdasarkan ID.
     */
    suspend fun deleteDosen(id: String): Result<Unit>
}

