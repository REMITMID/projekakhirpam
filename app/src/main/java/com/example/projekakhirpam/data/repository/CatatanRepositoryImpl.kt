package com.example.projekakhirpam.data.repository

import com.example.projekakhirpam.data.model.*
import com.example.projekakhirpam.domain.model.*
import com.example.projekakhirpam.domain.repository.CatatanRepository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order

class CatatanRepositoryImpl(
    private val client: SupabaseClient
) : CatatanRepository {

    private fun uid(): String {
        return client.auth.currentUserOrNull()?.id
            ?: error(
                "Sesi login tidak ditemukan. Silakan login kembali."
            )
    }

    // =============================
    // MATA KULIAH
    // =============================

    override suspend fun mataKuliah(
        semester: Int
    ): List<MataKuliahCatatan> {

        return client.from("catatan_mata_kuliah")
            .select {
                filter {
                    eq("user_id", uid())
                    eq("semester", semester)
                }
                order("nama", Order.ASCENDING)
            }
            .decodeList<MataKuliahDto>()
            .map { it.toDomain() }
    }

    override suspend fun tambahMataKuliah(
        semester: Int,
        nama: String
    ) {
        client.from("catatan_mata_kuliah")
            .insert(
                MataKuliahInsert(
                    userId = uid(),
                    semester = semester,
                    nama = nama.trim()
                )
            )
    }

    override suspend fun hapusMataKuliah(
        id: String
    ) {
        client.from("catatan_mata_kuliah")
            .delete {
                filter {
                    eq("id", id)
                    eq("user_id", uid())
                }
            }
    }

    // =============================
    // PERTEMUAN
    // =============================

    override suspend fun pertemuan(
        mataKuliahId: String
    ): List<PertemuanCatatan> {

        return client.from("catatan_pertemuan")
            .select {
                filter {
                    eq("user_id", uid())
                    eq("mata_kuliah_id", mataKuliahId)
                }
                order("nomor", Order.ASCENDING)
            }
            .decodeList<PertemuanDto>()
            .map { it.toDomain() }
    }

    override suspend fun tambahPertemuan(
        mataKuliahId: String,
        nomor: Int
    ) {
        client.from("catatan_pertemuan")
            .insert(
                PertemuanInsert(
                    userId = uid(),
                    mataKuliahId = mataKuliahId,
                    nomor = nomor
                )
            )
    }

    override suspend fun hapusPertemuan(
        id: String
    ) {
        client.from("catatan_pertemuan")
            .delete {
                filter {
                    eq("id", id)
                    eq("user_id", uid())
                }
            }
    }

    // =============================
    // CATATAN AKADEMIK
    // =============================

    override suspend fun catatan(
        pertemuanId: String
    ): List<CatatanAkademik> {

        return client.from("catatan_isi")
            .select {
                filter {
                    eq("user_id", uid())
                    eq("pertemuan_id", pertemuanId)
                }
                order("created_at", Order.DESCENDING)
            }
            .decodeList<CatatanDto>()
            .map { it.toDomain() }
    }

    override suspend fun simpanCatatan(
        pertemuanId: String,
        id: String?,
        judul: String,
        isi: String
    ) {
        if (id == null) {

            // Tambah catatan baru
            client.from("catatan_isi")
                .insert(
                    CatatanInsert(
                        userId = uid(),
                        pertemuanId = pertemuanId,
                        judul = judul.trim(),
                        isi = isi.trim()
                    )
                )

        } else {

            // Edit catatan yang sudah ada
            client.from("catatan_isi")
                .update(
                    CatatanUpdate(
                        judul = judul.trim(),
                        isi = isi.trim()
                    )
                ) {
                    filter {
                        eq("id", id)
                        eq("user_id", uid())
                        eq("pertemuan_id", pertemuanId)
                    }
                }
        }
    }

    override suspend fun hapusCatatan(
        id: String
    ) {
        client.from("catatan_isi")
            .delete {
                filter {
                    eq("id", id)
                    eq("user_id", uid())
                }
            }
    }
}