package com.example.projekakhirpam.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Referensi(
    @SerialName("id") val id: Int = 0, // Atau String jika menggunakan UUID
    @SerialName("user_id") val userId: String = "",
    @SerialName("judul") val judul: String,
    @SerialName("penulis") val penulis: String,
    @SerialName("tahun_terbit") val tahunTerbit: String,
    @SerialName("kategori") val kategori: String,
    @SerialName("url_referensi") val urlReferensi: String,
    @SerialName("catatan") val catatan: String,
    @SerialName("created_at") val createdAt: String? = null
)

