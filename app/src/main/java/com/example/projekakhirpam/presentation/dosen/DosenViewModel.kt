package com.example.projekakhirpam.presentation.dosen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.data.repository.DemoDosenRepository
import com.example.projekakhirpam.domain.model.Dosen
import com.example.projekakhirpam.domain.repository.DosenRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel untuk Direktori Dosen.
 * Mengikuti best practice Modul MVVM:
 * - Unidirectional Data Flow (UDF)
 * - Teks TextField menggunakan mutableStateOf
 * - State gabungan diekspos melalui StateFlow read-only
 * - Hanya nama dosen yang wajib diisi pada saat pembuatan data baru
 */
class DosenViewModel(
    private val repository: DosenRepository = DemoDosenRepository()
) : ViewModel() {

    // (1) Query pencarian menggunakan mutableStateOf
    var query by mutableStateOf("")
        private set

    // (2) Status internal ViewModel
    private val _status = MutableStateFlow(DosenUiState(isLoading = true))

    // (3) UI State final: data repositori + filter pencarian + status layar
    val uiState: StateFlow<DosenUiState> = combine(
        repository.getDosenList(),
        _status,
        snapshotFlow { query }
    ) { dosenList, status, q ->
        val filteredList = if (q.isBlank()) {
            dosenList
        } else {
            dosenList.filter { dosen ->
                dosen.nama.contains(q, ignoreCase = true) ||
                dosen.mataKuliah.contains(q, ignoreCase = true) ||
                dosen.karakter.any { it.contains(q, ignoreCase = true) }
            }
        }
        status.copy(
            daftar = filteredList,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DosenUiState(isLoading = true)
    )

    fun onQueryChange(newQuery: String) {
        query = newQuery
    }

    fun clearQuery() {
        query = ""
    }

    fun loadDosenDetail(id: String) {
        viewModelScope.launch {
            _status.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val dosen = repository.getDosenById(id)
                _status.update { it.copy(selectedDosen = dosen, isLoading = false) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _status.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Gagal memuat detail dosen"
                    )
                }
            }
        }
    }

    /**
     * Menambahkan dosen baru.
     * Hanya nama dosen yang menjadi syarat wajib.
     */
    fun insertDosen(dosen: Dosen, onSuccess: () -> Unit) {
        if (dosen.nama.isBlank()) {
            _status.update { it.copy(errorMessage = "Nama Dosen wajib diisi") }
            return
        }

        viewModelScope.launch {
            _status.update { it.copy(isSaving = true, errorMessage = null) }
            try {
                val result = repository.insertDosen(dosen)
                if (result.isSuccess) {
                    _status.update {
                        it.copy(
                            isSaving = false,
                            successMessage = "Dosen berhasil ditambahkan"
                        )
                    }
                    onSuccess()
                } else {
                    _status.update {
                        it.copy(
                            isSaving = false,
                            errorMessage = result.exceptionOrNull()?.message ?: "Gagal menambahkan dosen"
                        )
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _status.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = e.message ?: "Terjadi kesalahan saat menyimpan"
                    )
                }
            }
        }
    }

    /**
     * Memperbarui data dosen.
     */
    fun updateDosen(id: String, dosen: Dosen, onSuccess: () -> Unit) {
        if (dosen.nama.isBlank()) {
            _status.update { it.copy(errorMessage = "Nama Dosen tidak boleh kosong") }
            return
        }

        viewModelScope.launch {
            _status.update { it.copy(isSaving = true, errorMessage = null) }
            try {
                val result = repository.updateDosen(id, dosen)
                if (result.isSuccess) {
                    _status.update {
                        it.copy(
                            isSaving = false,
                            selectedDosen = dosen,
                            successMessage = "Data dosen berhasil diperbarui"
                        )
                    }
                    onSuccess()
                } else {
                    _status.update {
                        it.copy(
                            isSaving = false,
                            errorMessage = result.exceptionOrNull()?.message ?: "Gagal memperbarui data"
                        )
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _status.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = e.message ?: "Terjadi kesalahan saat memperbarui"
                    )
                }
            }
        }
    }

    /**
     * Menghapus dosen dari direktori.
     */
    fun deleteDosen(id: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _status.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val result = repository.deleteDosen(id)
                if (result.isSuccess) {
                    _status.update {
                        it.copy(
                            isLoading = false,
                            selectedDosen = null,
                            successMessage = "Dosen berhasil dihapus"
                        )
                    }
                    onSuccess()
                } else {
                    _status.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.exceptionOrNull()?.message ?: "Gagal menghapus dosen"
                        )
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _status.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Terjadi kesalahan saat menghapus"
                    )
                }
            }
        }
    }

    fun dismissError() {
        _status.update { it.copy(errorMessage = null) }
    }

    fun dismissSuccess() {
        _status.update { it.copy(successMessage = null) }
    }
}

