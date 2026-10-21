package com.example.projekakhirpam.data.model

import com.example.projekakhirpam.domain.model.AktivitasKampus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AktivitasKampusDto(
    @SerialName("id") val id: String? = null,
    @SerialName("user_id") val userId: String? = null,
    @SerialName("judul_kegiatan") val judulKegiatan: String,
    @SerialName("kategori") val kategori: String,
    @SerialName("tanggal_pelaksanaan") val tanggalPelaksanaan: String,
    @SerialName("lokasi") val lokasi: String,
    @SerialName("penyelenggara") val penyelenggara: String,
    @SerialName("deskripsi") val deskripsi: String,
    @SerialName("status_partisipasi") val statusPartisipasi: String
)

fun AktivitasKampusDto.toDomain() = AktivitasKampus(
    id, userId, judulKegiatan, kategori, tanggalPelaksanaan, lokasi, penyelenggara, deskripsi, statusPartisipasi
)

fun AktivitasKampus.toDto() = AktivitasKampusDto(
    id, userId, judulKegiatan, kategori, tanggalPelaksanaan, lokasi, penyelenggara, deskripsi, statusPartisipasi
)