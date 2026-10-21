package com.example.projekakhirpam.presentation.aktivitas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projekakhirpam.domain.model.AktivitasKampus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AktivitasFormScreen(
    initialData: AktivitasKampus? = null,
    onSubmit: (AktivitasKampus) -> Unit,
    onBackClick: () -> Unit
) {
    var judul by remember { mutableStateOf(initialData?.judulKegiatan ?: "") }
    var kategori by remember { mutableStateOf(initialData?.kategori ?: "") }
    var tanggal by remember { mutableStateOf(initialData?.tanggalPelaksanaan ?: "") }
    var lokasi by remember { mutableStateOf(initialData?.lokasi ?: "") }
    var penyelenggara by remember { mutableStateOf(initialData?.penyelenggara ?: "") }
    var deskripsi by remember { mutableStateOf(initialData?.deskripsi ?: "") }
    var status by remember { mutableStateOf(initialData?.statusPartisipasi ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (initialData == null) "tambah aktivitas" else "edit aktivitas") },
                navigationIcon = {
                    TextButton(onClick = onBackClick) { Text("batal") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(value = judul, onValueChange = { judul = it }, label = { Text("judul kegiatan") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = kategori, onValueChange = { kategori = it }, label = { Text("kategori") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = tanggal, onValueChange = { tanggal = it }, label = { Text("tanggal pelaksanaan") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = lokasi, onValueChange = { lokasi = it }, label = { Text("lokasi") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = penyelenggara, onValueChange = { penyelenggara = it }, label = { Text("penyelenggara") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = status, onValueChange = { status = it }, label = { Text("status partisipasi") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = deskripsi, onValueChange = { deskripsi = it }, label = { Text("deskripsi") }, modifier = Modifier.fillMaxWidth(), minLines = 3)

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (judul.isNotBlank()) {
                        onSubmit(
                            AktivitasKampus(
                                id = initialData?.id,
                                judulKegiatan = judul,
                                kategori = kategori,
                                tanggalPelaksanaan = tanggal,
                                lokasi = lokasi,
                                penyelenggara = penyelenggara,
                                deskripsi = deskripsi,
                                statusPartisipasi = status
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("simpan")
            }
        }
    }
}