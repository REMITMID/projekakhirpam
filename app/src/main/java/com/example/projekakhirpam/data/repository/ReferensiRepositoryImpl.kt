package com.example.projekakhirpam.data.repository

import com.example.projekakhirpam.domain.model.Referensi
import com.example.projekakhirpam.domain.repository.ReferensiRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
private data class ReferensiInsertDto(
    @SerialName("user_id") val userId: String,
    @SerialName("judul") val judul: String,
    @SerialName("penulis") val penulis: String,
    @SerialName("tahun_terbit") val tahunTerbit: String,
    @SerialName("kategori") val kategori: String,
    @SerialName("url_referensi") val urlReferensi: String,
    @SerialName("catatan") val catatan: String
)

class ReferensiRepositoryImpl(private val supabase: SupabaseClient) : ReferensiRepository {

    private val table = supabase.postgrest["koleksi_referensi"]

    private fun getUserId(): String {
        return supabase.auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("Pengguna belum login. Sesi tidak ditemukan.")
    }

    override suspend fun getSemuaReferensi(): Flow<List<Referensi>> = flow {
        val userId = supabase.auth.currentUserOrNull()?.id
        if (userId.isNullOrEmpty()) {
            emit(emptyList())
            return@flow
        }
        val referensiList = table.select {
            filter { eq("user_id", userId) }
            order("created_at", Order.DESCENDING)
        }.decodeList<Referensi>()
        emit(referensiList)
    }

    override suspend fun getReferensiById(id: Int): Referensi? {
        val userId = getUserId()
        return table.select {
            filter {
                eq("id", id)
                eq("user_id", userId)
            }
        }.decodeSingleOrNull<Referensi>()
    }

    override suspend fun tambahReferensi(referensi: Referensi) {
        val userId = getUserId()
        val dto = ReferensiInsertDto(
            userId = userId,
            judul = referensi.judul,
            penulis = referensi.penulis,
            tahunTerbit = referensi.tahunTerbit,
            kategori = referensi.kategori,
            urlReferensi = referensi.urlReferensi,
            catatan = referensi.catatan
        )
        table.insert(dto)
    }

    override suspend fun updateReferensi(referensi: Referensi) {
        val userId = getUserId()
        val item = referensi.copy(userId = userId)
        table.update(item) {
            filter {
                eq("id", referensi.id)
                eq("user_id", userId)
            }
        }
    }

    override suspend fun hapusReferensi(id: Int) {
        val userId = getUserId()
        table.delete {
            filter {
                eq("id", id)
                eq("user_id", userId)
            }
        }
    }
}
