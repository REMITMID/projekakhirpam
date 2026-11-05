package com.example.projekakhirpam.presentation.tugas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.projekakhirpam.core.network.SupabaseProvider
import com.example.projekakhirpam.data.repository.DemoTugasRepository
import com.example.projekakhirpam.data.repository.TUGAS_PAKAI_DUMMY
import com.example.projekakhirpam.data.repository.TugasRepositoryImpl
import com.example.projekakhirpam.domain.model.Tugas
import com.example.projekakhirpam.domain.model.TugasConst
import com.example.projekakhirpam.domain.repository.TugasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class TugasUiState {
    object Loading : TugasUiState()
    data class Success(val list: List<Tugas>) : TugasUiState()
    data class Error(val message: String) : TugasUiState()
}

class TugasViewModel(private val repository: TugasRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<TugasUiState>(TugasUiState.Loading)
    val uiState: StateFlow<TugasUiState> = _uiState.asStateFlow()

    // filter status di halaman list: "Semua", "To Do", "In Progress", "Completed"
    private val _filterStatus = MutableStateFlow(FILTER_SEMUA)
    val filterStatus: StateFlow<String> = _filterStatus.asStateFlow()

    // pesan error untuk ditampilkan sebagai Toast
    private val _pesanError = MutableStateFlow<String?>(null)
    val pesanError: StateFlow<String?> = _pesanError.asStateFlow()

    init { loadTugas() }

    fun loadTugas() {
        viewModelScope.launch {
            _uiState.value = TugasUiState.Loading
            repository.getTugasList()
                .onSuccess { _uiState.value = TugasUiState.Success(it) }
                .onFailure { _uiState.value = TugasUiState.Error(it.message ?: "gagal memuat tugas") }
        }
    }

    fun setFilter(status: String) {
        _filterStatus.value = status
    }

    fun addTugas(item: Tugas, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.insertTugas(item)
                .onSuccess { loadTugas(); onSuccess() }
                .onFailure { _pesanError.value = "Gagal menambah tugas: ${it.message}" }
        }
    }

    fun updateTugas(id: String, item: Tugas, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.updateTugas(id, item)
                .onSuccess { loadTugas(); onSuccess() }
                .onFailure { _pesanError.value = "Gagal mengubah tugas: ${it.message}" }
        }
    }

    fun deleteTugas(id: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.deleteTugas(id)
                .onSuccess { loadTugas(); onSuccess() }
                .onFailure { _pesanError.value = "Gagal menghapus tugas: ${it.message}" }
        }
    }

    // ubah status saja (dipakai checkbox di list dan chip status di detail)
    fun ubahStatus(item: Tugas, statusBaru: String) {
        val id = item.id ?: return
        val tugasBaru = item.copy(status = statusBaru)
        viewModelScope.launch {
            repository.updateTugas(id, tugasBaru)
                .onSuccess {
                    val current = _uiState.value
                    if (current is TugasUiState.Success) {
                        _uiState.value = TugasUiState.Success(
                            current.list.map { if (it.id == id) tugasBaru else it }
                        )
                    }
                }
                .onFailure { _pesanError.value = "Gagal mengubah status: ${it.message}" }
        }
    }

    fun toggleSelesai(item: Tugas) {
        val statusBaru = if (item.status == TugasConst.STATUS_COMPLETED) {
            TugasConst.STATUS_TODO
        } else {
            TugasConst.STATUS_COMPLETED
        }
        ubahStatus(item, statusBaru)
    }

    fun clearError() {
        _pesanError.value = null
    }

    companion object {
        const val FILTER_SEMUA = "Semua"

        /** Membuat ViewModel: pakai data dummy atau Supabase sesuai saklar TUGAS_PAKAI_DUMMY. */
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository: TugasRepository = if (TUGAS_PAKAI_DUMMY) {
                    DemoTugasRepository()
                } else {
                    TugasRepositoryImpl(SupabaseProvider.client)
                }
                TugasViewModel(repository)
            }
        }
    }
}
