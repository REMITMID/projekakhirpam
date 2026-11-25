package com.example.projekakhirpam.data.model

import com.example.projekakhirpam.domain.model.Dosen
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DosenDto(
    @SerialName("id") val id: String? = null,
    @SerialName("user_id") val userId: String? = null,
    @SerialName("nama") val nama: String,
    @SerialName("foto_url") val fotoUrl: String? = null,
    @SerialName("no_wa") val noWa: String? = "",
    @SerialName("email") val email: String? = "",
    @SerialName("mata_kuliah") val mataKuliah: String? = "",
    @SerialName("kontak_preferensi") val kontakPreferensi: String? = "WhatsApp",
    @SerialName("karakter") val karakter: List<String>? = emptyList(),
    @SerialName("catatan") val catatan: String? = ""
)

fun DosenDto.toDomain() = Dosen(
    id = id.orEmpty(),
    nama = nama,
    fotoUrl = fotoUrl,
    noWa = noWa.orEmpty(),
    email = email.orEmpty(),
    mataKuliah = mataKuliah.orEmpty(),
    kontakPreferensi = kontakPreferensi ?: "WhatsApp",
    karakter = karakter ?: emptyList(),
    catatan = catatan.orEmpty()
)

fun Dosen.toDto(userId: String) = DosenDto(
    id = if (id.isBlank() || id.startsWith("dosen-")) null else id,
    userId = userId,
    nama = nama,
    fotoUrl = fotoUrl,
    noWa = noWa,
    email = email,
    mataKuliah = mataKuliah,
    kontakPreferensi = kontakPreferensi,
    karakter = karakter,
    catatan = catatan
)
