package com.example.projekakhirpam.presentation.koleksireferensi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projekakhirpam.domain.model.Referensi
import com.example.projekakhirpam.presentation.components.aksaOutlinedTextFieldColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReferensiFormScreen(
    referensiId: Int? = null,
    viewModel: ReferensiViewModel,
    onNavigateBack: () -> Unit
) {
    val isEditMode = referensiId != null && referensiId != 0

    var judul by remember { mutableStateOf("") }
    var penulis by remember { mutableStateOf("") }
    var tahunTerbit by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }
    var urlReferensi by remember { mutableStateOf("") }
    var catatan by remember { mutableStateOf("") }

    var isSaving by remember { mutableStateOf(false) }
    var validationError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(referensiId) {
        if (referensiId != null && referensiId != 0) {
            viewModel.getReferensiById(referensiId) { item ->
                if (item != null) {
                    judul = item.judul
                    penulis = item.penulis
                    tahunTerbit = item.tahunTerbit
                    kategori = item.kategori
                    urlReferensi = item.urlReferensi
                    catatan = item.catatan
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit Referensi" else "Tambah Referensi", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (validationError != null) {
                Text(
                    text = validationError ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            OutlinedTextField(
                value = judul,
                onValueChange = {
                    judul = it
                    validationError = null
                },
                label = { Text("Judul Referensi *") },
                colors = aksaOutlinedTextFieldColors(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = penulis,
                onValueChange = {
                    penulis = it
                    validationError = null
                },
                label = { Text("Penulis *") },
                colors = aksaOutlinedTextFieldColors(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = tahunTerbit,
                onValueChange = { tahunTerbit = it },
                label = { Text("Tahun Terbit (mis. 2024)") },
                colors = aksaOutlinedTextFieldColors(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = kategori,
                onValueChange = { kategori = it },
                label = { Text("Kategori (Jurnal / Buku / Website)") },
                colors = aksaOutlinedTextFieldColors(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = urlReferensi,
                onValueChange = { urlReferensi = it },
                label = { Text("URL / Link Referensi") },
                colors = aksaOutlinedTextFieldColors(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = catatan,
                onValueChange = { catatan = it },
                label = { Text("Catatan Tambahan") },
                colors = aksaOutlinedTextFieldColors(),
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    if (judul.isBlank()) {
                        validationError = "Judul referensi tidak boleh kosong"
                        return@Button
                    }
                    if (penulis.isBlank()) {
                        validationError = "Nama penulis tidak boleh kosong"
                        return@Button
                    }
                    isSaving = true
                    val item = Referensi(
                        id = referensiId ?: 0,
                        judul = judul.trim(),
                        penulis = penulis.trim(),
                        tahunTerbit = tahunTerbit.trim(),
                        kategori = kategori.trim(),
                        urlReferensi = urlReferensi.trim(),
                        catatan = catatan.trim()
                    )
                    viewModel.simpanReferensi(item) {
                        isSaving = false
                        onNavigateBack()
                    }
                },
                enabled = !isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(if (isEditMode) "Simpan Perubahan" else "Tambah Referensi")
                }
            }
        }
    }
}
