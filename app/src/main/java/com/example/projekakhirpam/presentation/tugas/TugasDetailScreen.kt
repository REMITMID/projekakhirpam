package com.example.projekakhirpam.presentation.tugas

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projekakhirpam.domain.model.Tugas
import com.example.projekakhirpam.domain.model.TugasConst

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TugasDetailScreen(
    tugas: Tugas,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onStatusChange: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var tampilDialogHapus by remember { mutableStateOf(false) }
    val (latarPrioritas, teksPrioritas) = warnaPrioritas(tugas.prioritas)
    val terlambat = isTerlambat(tugas.deadline, tugas.status)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Tugas") },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = tugas.judul, style = MaterialTheme.typography.headlineSmall)
            Text(
                text = tugas.mataKuliah,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            LabelKecil("Prioritas ${tugas.prioritas}", latarPrioritas, teksPrioritas)

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Deadline: ${formatTanggal(tugas.deadline)}")
                    Text(
                        text = labelDeadline(tugas.deadline, tugas.status),
                        color = if (terlambat) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Ubah status langsung dari detail (UPDATE)
            Text("Status", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TugasConst.DAFTAR_STATUS.forEach { s ->
                    FilterChip(
                        selected = tugas.status == s,
                        onClick = { if (tugas.status != s) onStatusChange(s) },
                        label = { Text(s) }
                    )
                }
            }

            Text("Deskripsi", style = MaterialTheme.typography.titleMedium)
            Text(if (tugas.deskripsi.isBlank()) "Tidak ada deskripsi." else tugas.deskripsi)

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(onClick = onEditClick, modifier = Modifier.weight(1f)) {
                    Text("Edit")
                }
                Button(
                    onClick = { tampilDialogHapus = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Hapus")
                }
            }
        }
    }

    // Dialog konfirmasi hapus (DELETE)
    if (tampilDialogHapus) {
        AlertDialog(
            onDismissRequest = { tampilDialogHapus = false },
            title = { Text("Hapus tugas?") },
            text = { Text("Tugas \"${tugas.judul}\" akan dihapus permanen.") },
            confirmButton = {
                TextButton(onClick = {
                    tampilDialogHapus = false
                    onDeleteClick()
                }) {
                    Text("Hapus", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { tampilDialogHapus = false }) { Text("Batal") }
            }
        )
    }
}
