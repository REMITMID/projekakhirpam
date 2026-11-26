package com.example.projekakhirpam.data.repository

import com.example.projekakhirpam.core.network.SupabaseProvider
import com.example.projekakhirpam.data.model.JadwalDto
import com.example.projekakhirpam.data.model.toDomain
import com.example.projekakhirpam.data.model.toInsert
import com.example.projekakhirpam.data.model.toUpdate
import com.example.projekakhirpam.domain.model.Jadwal
import com.example.projekakhirpam.domain.repository.JadwalRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest

class JadwalRepositoryImp(
    private val supabase: SupabaseClient = SupabaseProvider.client
) : JadwalRepository {

    private fun uid(): String {
        return supabase.auth.currentUserOrNull()?.id
            ?: throw Exception("Sesi login tidak ditemukan. Silakan login kembali.")
    }

    override suspend fun getJadwalList(): Result<List<Jadwal>> = runCatching {
        val userId = uid()
        val response = supabase.postgrest["jadwal_kuliah"]
            .select { filter { eq("user_id", userId) } }
            .decodeList<JadwalDto>()

        response.map { it.toDomain() }
            .sortedWith(compareBy({ it.hari.ordinal }, { it.jamMulai }))
    }

    override suspend fun getJadwalById(id: String): Result<Jadwal?> = runCatching {
        val userId = uid()
        val response = supabase.postgrest["jadwal_kuliah"]
            .select {
                filter {
                    eq("id", id)
                    eq("user_id", userId)
                }
            }
            .decodeSingleOrNull<JadwalDto>()
        response?.toDomain()
    }

    override suspend fun insertJadwal(jadwal: Jadwal): Result<Unit> = runCatching {
        val userId = uid()
        supabase.postgrest["jadwal_kuliah"].insert(jadwal.toInsert(userId))
    }

    override suspend fun updateJadwal(id: String, jadwal: Jadwal): Result<Unit> = runCatching {
        val userId = uid()
        supabase.postgrest["jadwal_kuliah"].update(jadwal.toUpdate()) {
            filter {
                eq("id", id)
                eq("user_id", userId)
            }
        }
    }

    override suspend fun deleteJadwal(id: String): Result<Unit> = runCatching {
        val userId = uid()
        supabase.postgrest["jadwal_kuliah"].delete {
            filter {
                eq("id", id)
                eq("user_id", userId)
            }
        }
    }
}