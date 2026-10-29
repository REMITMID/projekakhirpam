package com.example.projekakhirpam.domain.repository

import com.example.projekakhirpam.domain.model.Referensi
import kotlinx.coroutines.flow.Flow

interface ReferensiRepository {
    suspend fun getSemuaReferensi(): Flow<List<Referensi>>
    suspend fun getReferensiById(id: Int): Referensi?
    suspend fun tambahReferensi(referensi: Referensi)
    suspend fun updateReferensi(referensi: Referensi)
    suspend fun hapusReferensi(id: Int)
}