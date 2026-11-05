package com.example.projekakhirpam.presentation.tugas

import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.projekakhirpam.domain.model.Tugas
import com.example.projekakhirpam.domain.model.TugasConst

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TugasFormScreen(
    viewModel: TugasViewModel,
    initialData: Tugas? = null,
    onSaved: () -> Unit,
    onBackClick: () -> Unit
) {
    var judul by remember { mutableStateOf(initialData?.judul ?: "") }
    var mataKuliah by remember { mutableStateOf(initialData?.mataKuliah ?: "") }
    var deadline by remember { mutableStateOf(initialData?.deadline ?: "") }
    var prioritas by remember { mutableStateOf(initialData?.prioritas ?: TugasConst.PRIORITAS_MEDIUM) }
    var status by remember { mutableStateOf(initialData?.status ?: TugasConst.STATUS_TODO) }
    var deskripsi by remember { mutableStateOf(initialData?.deskripsi ?: "") }

    var sudahSubmit by remember { mutableStateOf(false) }
    var menyimpan by remember { mutableStateOf(false) }

    // tampilkan error dari ViewModel
    val pesanError by viewModel.pesanError.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(pesanError) {
        pesanError?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearError()
            menyimpan = false
        }
    }

    val judulError = sudahSubmit && judul.isBlank()
    val mataKuliahError = sudahSubmit && mataKuliah.isBlank()
    val deadlineError = sudahSubmit && !isTanggalValid(deadline)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (initialData == null) "Tambah Tugas" else "Edit Tugas") },
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
            OutlinedTextField(
                value = judul,
                onValueChange = { judul = it },
                label = { Text("Judul tugas") },
                isError = judulError,
                supportingText = { if (judulError) Text("Judul wajib diisi") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = mataKuliah,
                onValueChange = { mataKuliah = it },
                label = { Text("Mata kuliah") },
                isError = mataKuliahError,
                supportingText = { if (mataKuliahError) Text("Mata kuliah wajib diisi") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = deadline,
                onValueChange = { deadline = it.trim() },
                label = { Text("Deadline") },
                placeholder = { Text("2026-11-20") },
                isError = deadlineError,
                supportingText = {
                    Text(if (deadlineError) "Format salah, gunakan TTTT-BB-HH" else "Format: TTTT-BB-HH")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Text("Prioritas", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TugasConst.DAFTAR_PRIORITAS.forEach { p ->
                    FilterChip(
                        selected = prioritas == p,
                        onClick = { prioritas = p },
                        label = { Text(p) }
                    )
                }
            }

            Text("Status", style = MaterialTheme.typography.titleSmall)
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TugasConst.DAFTAR_STATUS.forEach { s ->
                    FilterChip(
                        selected = status == s,
                        onClick = { status = s },
                        label = { Text(s) }
                    )
                }
            }

            OutlinedTextField(
                value = deskripsi,
                onValueChange = { deskripsi = it },
                label = { Text("Deskripsi (opsional)") },
                minLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    sudahSubmit = true
                    if (judul.isNotBlank() && mataKuliah.isNotBlank() && isTanggalValid(deadline)) {
                        menyimpan = true
                        val tugas = Tugas(
                            id = initialData?.id,
                            userId = initialData?.userId,
                            judul = judul.trim(),
                            mataKuliah = mataKuliah.trim(),
                            deskripsi = deskripsi.trim(),
                            deadline = deadline,
                            prioritas = prioritas,
                            status = status
                        )
                        val idLama = initialData?.id
                        if (idLama == null) {
                            viewModel.addTugas(tugas, onSaved)
                        } else {
                            viewModel.updateTugas(idLama, tugas, onSaved)
                        }
                    }
                },
                enabled = !menyimpan,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (menyimpan) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text(if (initialData == null) "Simpan" else "Simpan Perubahan")
                }
            }
        }
    }
}
