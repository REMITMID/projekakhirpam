package com.example.projekakhirpam.domain.repository

import com.example.projekakhirpam.domain.model.Tugas

interface TugasRepository {
    suspend fun getTugasList(): Result<List<Tugas>>
    suspend fun insertTugas(item: Tugas): Result<Unit>
    suspend fun updateTugas(id: String, item: Tugas): Result<Unit>
    suspend fun deleteTugas(id: String): Result<Unit>
}
