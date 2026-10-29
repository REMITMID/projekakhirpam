package com.example.projekakhirpam.presentation.catatan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.projekakhirpam.domain.model.*
import com.example.projekakhirpam.domain.repository.CatatanRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CatatanUiState(
    val semester: Int = 1,
    val mataKuliah: List<MataKuliahCatatan> = emptyList(),
    val pertemuan: List<PertemuanCatatan> = emptyList(),
    val catatan: List<CatatanAkademik> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)

class CatatanViewModel(
    private val repo: CatatanRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(CatatanUiState())
    val ui = _ui.asStateFlow()

    private fun task(action: suspend () -> Unit) =
        viewModelScope.launch {
            _ui.value = _ui.value.copy(
                loading = true,
                error = null
            )

            try {
                action()
            } catch (e: Exception) {
                _ui.value = _ui.value.copy(
                    error = e.message ?: "Terjadi kesalahan"
                )
            } finally {
                _ui.value = _ui.value.copy(loading = false)
            }
        }

    // SEMESTER

    fun pilihSemester(semester: Int) = task {
        _ui.value = _ui.value.copy(
            semester = semester,
            mataKuliah = emptyList(),
            pertemuan = emptyList(),
            catatan = emptyList()
        )

        _ui.value = _ui.value.copy(
            mataKuliah = repo.mataKuliah(semester)
        )
    }

    // MATA KULIAH

    fun tambahMataKuliah(
        nama: String,
        selesai: () -> Unit
    ) = task {
        require(nama.isNotBlank()) {
            "Nama mata kuliah wajib diisi"
        }

        repo.tambahMataKuliah(
            ui.value.semester,
            nama
        )

        _ui.value = _ui.value.copy(
            mataKuliah = repo.mataKuliah(ui.value.semester)
        )

        selesai()
    }

    fun hapusMataKuliah(
        id: String,
        selesai: () -> Unit
    ) = task {
        repo.hapusMataKuliah(id)

        _ui.value = _ui.value.copy(
            mataKuliah = repo.mataKuliah(ui.value.semester)
        )

        selesai()
    }

    // PERTEMUAN

    fun bukaMataKuliah(id: String) = task {
        _ui.value = _ui.value.copy(
            pertemuan = emptyList(),
            catatan = emptyList()
        )

        _ui.value = _ui.value.copy(
            pertemuan = repo.pertemuan(id)
        )
    }

    fun tambahPertemuan(
        mataKuliahId: String,
        nomor: Int,
        selesai: () -> Unit
    ) = task {
        require(nomor > 0) {
            "Nomor pertemuan harus lebih dari 0"
        }

        repo.tambahPertemuan(
            mataKuliahId,
            nomor
        )

        _ui.value = _ui.value.copy(
            pertemuan = repo.pertemuan(mataKuliahId)
        )

        selesai()
    }

    fun hapusPertemuan(
        mataKuliahId: String,
        id: String,
        selesai: () -> Unit
    ) = task {
        repo.hapusPertemuan(id)

        _ui.value = _ui.value.copy(
            pertemuan = repo.pertemuan(mataKuliahId)
        )

        selesai()
    }

    // CATATAN

    fun bukaPertemuan(id: String) = task {
        _ui.value = _ui.value.copy(
            catatan = emptyList()
        )

        _ui.value = _ui.value.copy(
            catatan = repo.catatan(id)
        )
    }

    fun simpanCatatan(
        pertemuanId: String,
        id: String?,
        judul: String,
        isi: String,
        selesai: () -> Unit
    ) = task {
        require(judul.isNotBlank()) {
            "Judul catatan wajib diisi"
        }

        require(isi.isNotBlank()) {
            "Isi catatan wajib diisi"
        }

        repo.simpanCatatan(
            pertemuanId,
            id,
            judul,
            isi
        )

        _ui.value = _ui.value.copy(
            catatan = repo.catatan(pertemuanId)
        )

        selesai()
    }

    fun hapusCatatan(
        pertemuanId: String,
        id: String,
        selesai: () -> Unit
    ) = task {
        repo.hapusCatatan(id)

        _ui.value = _ui.value.copy(
            catatan = repo.catatan(pertemuanId)
        )

        selesai()
    }

    fun bersihkanError() {
        _ui.value = _ui.value.copy(error = null)
    }
}