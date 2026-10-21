package com.example.projekakhirpam.domain.repository

import com.example.projekakhirpam.domain.model.AktivitasKampus

interface AktivitasRepository {
    suspend fun getAktivitasList(): Result<List<AktivitasKampus>>
    suspend fun insertAktivitas(item: AktivitasKampus): Result<Unit>
    suspend fun updateAktivitas(id: String, item: AktivitasKampus): Result<Unit>
    suspend fun deleteAktivitas(id: String): Result<Unit>
}