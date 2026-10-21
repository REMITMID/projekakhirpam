package com.example.projekakhirpam.presentation.aktivitas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projekakhirpam.domain.model.AktivitasKampus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AktivitasDetailScreen(
    item: AktivitasKampus,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("detail aktivitas") },
                navigationIcon = {
                    TextButton(onClick = onBackClick) { Text("kembali") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = item.judulKegiatan, style = MaterialTheme.typography.headlineMedium)

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(text = "kategori: ${item.kategori}")
                    Text(text = "penyelenggara: ${item.penyelenggara}")
                    Text(text = "tanggal: ${item.tanggalPelaksanaan}")
                    Text(text = "lokasi: ${item.lokasi}")
                    Text(text = "status: ${item.statusPartisipasi}")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "deskripsi:", style = MaterialTheme.typography.titleMedium)
            Text(text = item.deskripsi)

            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(onClick = onEditClick, modifier = Modifier.weight(1f)) {
                    Text("edit")
                }
                Button(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("hapus")
                }
            }
        }
    }
}