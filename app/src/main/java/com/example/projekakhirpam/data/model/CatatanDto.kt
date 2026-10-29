package com.example.projekakhirpam.data.model

import com.example.projekakhirpam.domain.model.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MataKuliahDto(
    val id: String,
    val semester: Int,
    val nama: String,

    @SerialName("user_id")
    val userId: String
) {
    fun toDomain() = MataKuliahCatatan(
        id = id,
        semester = semester,
        nama = nama
    )
}

@Serializable
data class PertemuanDto(
    val id: String,

    @SerialName("mata_kuliah_id")
    val mataKuliahId: String,

    val nomor: Int,

    @SerialName("user_id")
    val userId: String
) {
    fun toDomain() = PertemuanCatatan(
        id = id,
        mataKuliahId = mataKuliahId,
        nomor = nomor
    )
}

@Serializable
data class CatatanDto(
    val id: String,

    @SerialName("pertemuan_id")
    val pertemuanId: String,

    val judul: String,
    val isi: String,

    @SerialName("user_id")
    val userId: String,

    @SerialName("created_at")
    val createdAt: String = ""
) {
    fun toDomain() = CatatanAkademik(
        id = id,
        pertemuanId = pertemuanId,
        judul = judul,
        isi = isi,
        createdAt = createdAt
    )
}

// DTO untuk menambah mata kuliah
@Serializable
data class MataKuliahInsert(
    @SerialName("user_id")
    val userId: String,

    val semester: Int,
    val nama: String
)

// DTO untuk menambah pertemuan
@Serializable
data class PertemuanInsert(
    @SerialName("user_id")
    val userId: String,

    @SerialName("mata_kuliah_id")
    val mataKuliahId: String,

    val nomor: Int
)

// DTO untuk menambah catatan
@Serializable
data class CatatanInsert(
    @SerialName("user_id")
    val userId: String,

    @SerialName("pertemuan_id")
    val pertemuanId: String,

    val judul: String,
    val isi: String
)

// DTO untuk mengedit catatan
@Serializable
data class CatatanUpdate(
    val judul: String,
    val isi: String
)