package com.example.projekakhirpam.data.repository

import com.example.projekakhirpam.data.model.TugasDto
import com.example.projekakhirpam.data.model.toDomain
import com.example.projekakhirpam.data.model.toDto
import com.example.projekakhirpam.domain.model.Tugas
import com.example.projekakhirpam.domain.repository.TugasRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order

class TugasRepositoryImpl(private val supabase: SupabaseClient) : TugasRepository {

    private val namaTabel = "tasks"

    private fun userIdAktif(): String =
        supabase.auth.currentUserOrNull()?.id ?: throw Exception("user belum login")

    // READ: tugas milik user aktif, diurutkan dari deadline terdekat
    override suspend fun getTugasList(): Result<List<Tugas>> = runCatching {
        val userId = userIdAktif()
        supabase.postgrest[namaTabel]
            .select {
                filter { eq("user_id", userId) }
                order("deadline", Order.ASCENDING)
            }
            .decodeList<TugasDto>()
            .map { it.toDomain() }
    }

    // INSERT: tambah tugas baru
    override suspend fun insertTugas(item: Tugas): Result<Unit> = runCatching {
        val dto = item.copy(id = null, userId = userIdAktif()).toDto()
        supabase.postgrest[namaTabel].insert(dto)
    }

    // UPDATE: ubah detail tugas atau statusnya
    override suspend fun updateTugas(id: String, item: Tugas): Result<Unit> = runCatching {
        val userId = userIdAktif()
        val dto = item.copy(id = id, userId = userId).toDto()
        supabase.postgrest[namaTabel].update(dto) {
            filter {
                eq("id", id)
                eq("user_id", userId)
            }
        }
    }

    // DELETE: hapus tugas
    override suspend fun deleteTugas(id: String): Result<Unit> = runCatching {
        val userId = userIdAktif()
        supabase.postgrest[namaTabel].delete {
            filter {
                eq("id", id)
                eq("user_id", userId)
            }
        }
    }
}
