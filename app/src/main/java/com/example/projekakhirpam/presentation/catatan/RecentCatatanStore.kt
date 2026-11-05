package com.example.projekakhirpam.presentation.catatan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/** Lightweight UI state shared by Home and the Catatan feature. */
data class RecentCatatan(
    val id: String,
    val judul: String,
    val isi: String,
    val mataKuliahId: String,
    val mataKuliahNama: String,
    val semester: Int,
    val pertemuanId: String,
    val pertemuanNomor: Int,
    val lastOpenedAt: Long = System.currentTimeMillis()
)

object RecentCatatanStore {
    var recent by mutableStateOf<RecentCatatan?>(null)
        private set

    var openRequest by mutableStateOf<RecentCatatan?>(null)
        private set

    fun markOpened(note: RecentCatatan) {
        recent = note.copy(lastOpenedAt = System.currentTimeMillis())
    }

    fun requestOpenRecent() {
        openRequest = recent
    }

    fun consumeOpenRequest(): RecentCatatan? {
        val request = openRequest
        openRequest = null
        return request
    }

    fun forget(noteId: String) {
        if (recent?.id == noteId) recent = null
        if (openRequest?.id == noteId) openRequest = null
    }
}
