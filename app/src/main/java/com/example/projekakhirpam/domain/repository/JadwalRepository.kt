package com.example.projekakhirpam.domain.repository

import com.example.projekakhirpam.domain.model.Jadwal

interface JadwalRepository {
    suspend fun getJadwalList(): Result<List<Jadwal>>
    suspend fun getJadwalById(id: String): Result<Jadwal?>
    suspend fun insertJadwal(jadwal: Jadwal): Result<Unit>
    suspend fun updateJadwal(id: String, jadwal: Jadwal): Result<Unit>
    suspend fun deleteJadwal(id: String): Result<Unit>
}