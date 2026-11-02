package com.example.projekakhirpam.presentation.koleksireferensi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.domain.model.Referensi
import com.example.projekakhirpam.domain.repository.ReferensiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ReferensiViewModel(private val repository: ReferensiRepository) : ViewModel() {

    private val _referensiList = MutableStateFlow<List<Referensi>>(emptyList())
    val referensiList: StateFlow<List<Referensi>> = _referensiList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadReferensi()
    }

    fun loadReferensi() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // Ambil snapshot pertama. Repository demo memakai StateFlow yang
                // tidak selesai, sehingga collect() membuat loading terus aktif.
                _referensiList.value = repository.getSemuaReferensi().first()
            } catch (e: Exception) {
                _error.value = e.message ?: "Gagal memuat data referensi"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun hapusReferensi(id: Int) {
        viewModelScope.launch {
            repository.hapusReferensi(id)
            loadReferensi() // Refresh data setelah dihapus
        }
    }

    fun getReferensiById(id: Int, onLoaded: (Referensi?) -> Unit) {
        viewModelScope.launch {
            onLoaded(repository.getReferensiById(id))
        }
    }

    fun simpanReferensi(referensi: Referensi, onSaved: () -> Unit) {
        viewModelScope.launch {
            if (referensi.id == 0) repository.tambahReferensi(referensi)
            else repository.updateReferensi(referensi)
            loadReferensi()
            onSaved()
        }
    }
}
