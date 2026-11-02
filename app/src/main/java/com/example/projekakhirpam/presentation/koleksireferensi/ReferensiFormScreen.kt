package com.example.projekakhirpam.presentation.koleksireferensi

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReferensiFormScreen(
    referensiId: Int? = null, // Jika null berarti mode "Tambah", jika ada berarti "Edit"
    viewModel: ReferensiViewModel,
    onNavigateBack: () -> Unit
) {
    var judul by remember { mutableStateOf("") }
    var penulis by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text(if (referensiId == null) "Tambah Referensi" else "Edit Referensi") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = judul,
                onValueChange = { judul = it },
                label = { Text("Judul Referensi") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = penulis,
                onValueChange = { penulis = it },
                label = { Text("Penulis") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = kategori,
                onValueChange = { kategori = it },
                label = { Text("Kategori (Jurnal/Buku/Web)") },
                modifier = Modifier.fillMaxWidth()
            )
            // Tambahkan TextField lain untuk Tahun, URL, dan Catatan

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    // Panggil fungsi simpan/update dari ViewModel di sini
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simpan")
            }
        }
    }
}