package com.example.projekakhirpam.data.repository

import com.example.projekakhirpam.domain.model.Hari
import com.example.projekakhirpam.domain.model.Jadwal
import com.example.projekakhirpam.domain.repository.JadwalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class JadwalRepositoryImp : JadwalRepository {

    companion object {
        private val _store = MutableStateFlow(
            listOf(
                Jadwal("1", "demo-user", "Pemrograman Mobile", Hari.SENIN, "08:00", "10:30", "G 1.1", "Adit"),
                Jadwal("2", "demo-user", "Basis Data", Hari.SENIN, "13:00", "15:00", "F 2.12", "Hakim"),
                Jadwal("3", "demo-user", "Rekayasa Perangkat Lunak", Hari.SELASA, "09:00", "11:30", "F 3.2", "Najwan"),
                Jadwal("4", "demo-user", "Jaringan Komputer", Hari.RABU, "10:00", "12:30", "F 4.8", "Jack"),
                Jadwal("5", "demo-user", "Kecerdasan Buatan", Hari.KAMIS, "13:00", "15:30", "F 2.1", "Afham"),
                Jadwal("6", "demo-user", "Etika Profesi", Hari.JUMAT, "08:00", "09:40", "F 3.2", "Luhung")
            )
        )
    }

    override suspend fun getJadwalList(): Result<List<Jadwal>> = runCatching {
        _store.value.sortedWith(compareBy({ it.hari.ordinal }, { it.jamMulai }))
    }

    override suspend fun getJadwalById(id: String): Result<Jadwal?> = runCatching {
        _store.value.find { it.id == id }
    }

    override suspend fun insertJadwal(jadwal: Jadwal): Result<Unit> = runCatching {
        val newId = if (jadwal.id.isBlank()) UUID.randomUUID().toString() else jadwal.id
        val newItem = jadwal.copy(id = newId, userId = "demo-user")
        _store.update { current -> current + newItem }
    }

    override suspend fun updateJadwal(id: String, jadwal: Jadwal): Result<Unit> = runCatching {
        val updatedItem = jadwal.copy(id = id, userId = "demo-user")
        _store.update { current -> current.map { if (it.id == id) updatedItem else it } }
    }

    override suspend fun deleteJadwal(id: String): Result<Unit> = runCatching {
        _store.update { current -> current.filterNot { it.id == id } }
    }
}