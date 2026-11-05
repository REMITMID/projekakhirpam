package com.example.projekakhirpam.domain.model

/**
 * Entitas domain untuk Direktori Dosen.
 * Sesuai spesifikasi Proposal Proyek AKSA (Fitur 4: Direktori Dosen).
 *
 * @property id Identifikasi unik dosen
 * @property nama Nama lengkap dosen beserta gelar akademik (Wajib)
 * @property fotoUrl URI string atau URL foto profil dosen (Opsional)
 * @property noWa Nomor kontak WhatsApp (Opsional)
 * @property email Alamat email dosen (Opsional)
 * @property mataKuliah Mata kuliah utama yang diampu (Opsional)
 * @property kontakPreferensi Preferensi cara dihubungi (WhatsApp, Telegram, Email, Tatap Muka)
 * @property karakter Tag karakter pengajaran dosen (maksimal 3 tag)
 * @property catatan Catatan bebas, tips pengumpulan tugas, atau kebiasaan mengajar
 */
data class Dosen(
    val id: String,
    val nama: String,
    val fotoUrl: String? = null,
    val noWa: String = "",
    val email: String = "",
    val mataKuliah: String = "",
    val kontakPreferensi: String = "WhatsApp",
    val karakter: List<String> = emptyList(),
    val catatan: String = ""
)

