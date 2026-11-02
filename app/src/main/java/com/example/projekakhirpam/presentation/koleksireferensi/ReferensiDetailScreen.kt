package com.example.projekakhirpam.presentation.koleksireferensi

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReferensiDetailScreen(
    referensiId: Int,
    viewModel: ReferensiViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Int) -> Unit
) {
    // Di aplikasi nyata, kamu bisa mengambil data detail dari viewModel berdasarkan ID
    // Contoh sederhana UI:
    Scaffold(
        topBar = { TopAppBar(title = { Text("Detail Referensi") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Judul Jurnal/Buku", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Penulis: Nama Penulis")
            Text("Tahun: 2026")
            Text("Kategori: Jurnal")
            Text("URL: https://example.com")
            Spacer(modifier = Modifier.height(16.dp))
            Text("Catatan: Ini adalah catatan referensi...")

            Spacer(modifier = Modifier.weight(1f))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedButton(onClick = { onNavigateToEdit(referensiId) }) {
                    Text("Edit")
                }
                Button(onClick = {
                    viewModel.hapusReferensi(referensiId)
                    onNavigateBack()
                }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                    Text("Hapus")
                }
            }
        }
    }
}