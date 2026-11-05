package com.example.projekakhirpam.presentation.jadwal

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.projekakhirpam.domain.model.Hari
import com.example.projekakhirpam.domain.model.Jadwal
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JadwalFormScreen(
    initialData: Jadwal? = null,
    onSubmit: (Jadwal) -> Unit,
    onBackClick: () -> Unit
) {
    val isEditMode = initialData != null
    val colors = MaterialTheme.colorScheme
    val context = LocalContext.current

    var namaMatkul by remember(initialData) { mutableStateOf(initialData?.namaMatkul.orEmpty()) }
    var selectedHari by remember(initialData) { mutableStateOf(initialData?.hari ?: Hari.SENIN) }
    var jamMulai by remember(initialData) { mutableStateOf(initialData?.jamMulai.orEmpty()) }
    var jamSelesai by remember(initialData) { mutableStateOf(initialData?.jamSelesai.orEmpty()) }
    var ruangan by remember(initialData) { mutableStateOf(initialData?.ruangan.orEmpty()) }
    var dosen by remember(initialData) { mutableStateOf(initialData?.dosen.orEmpty()) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun showTimePicker(currentTime: String, onTimeSelected: (String) -> Unit) {
        val parts = currentTime.split(":")
        val initialHour = parts.getOrNull(0)?.toIntOrNull() ?: 8
        val initialMinute = parts.getOrNull(1)?.toIntOrNull() ?: 0

        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val formatted = String.format(Locale.ROOT, "%02d:%02d", hourOfDay, minute)
                onTimeSelected(formatted)
            },
            initialHour,
            initialMinute,
            true
        ).show()
    }

    Scaffold(
        containerColor = colors.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEditMode) "Ubah Jadwal Perkuliahan" else "Tambah Jadwal Perkuliahan",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = namaMatkul,
                onValueChange = {
                    namaMatkul = it
                    errorMessage = null
                },
                label = { Text("Nama Mata Kuliah *") },
                placeholder = { Text("Contoh: Pemrograman Mobile") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Hari Perkuliahan *",
                    style = MaterialTheme.typography.labelLarge,
                    color = colors.onSurfaceVariant
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(Hari.entries.toTypedArray()) { hari ->
                        FilterChip(
                            selected = selectedHari == hari,
                            onClick = { selectedHari = hari },
                            label = { Text(hari.label) }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = jamMulai,
                    onValueChange = { input ->
                        jamMulai = formatTimeInput(input, jamMulai)
                        errorMessage = null
                    },
                    label = { Text("Jam Mulai *") },
                    placeholder = { Text("08:00") },
                    supportingText = { Text("Contoh: 08:00") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            showTimePicker(jamMulai) { selected ->
                                jamMulai = selected
                                errorMessage = null
                            }
                        }) {
                            Icon(Icons.Default.Schedule, contentDescription = "Pilih Jam Mulai")
                        }
                    },
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = jamSelesai,
                    onValueChange = { input ->
                        jamSelesai = formatTimeInput(input, jamSelesai)
                        errorMessage = null
                    },
                    label = { Text("Jam Selesai *") },
                    placeholder = { Text("10:00") },
                    supportingText = { Text("Contoh: 10:00") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            showTimePicker(jamSelesai) { selected ->
                                jamSelesai = selected
                                errorMessage = null
                            }
                        }) {
                            Icon(Icons.Default.Schedule, contentDescription = "Pilih Jam Selesai")
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = ruangan,
                onValueChange = { ruangan = it },
                label = { Text("Ruangan Kelas") },
                placeholder = { Text("Contoh: Lab Komputer 2") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = dosen,
                onValueChange = { dosen = it },
                label = { Text("Nama Dosen Pengampu") },
                placeholder = { Text("Contoh: Dr. Budi Santoso") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            errorMessage?.let { error ->
                Text(
                    text = error,
                    color = colors.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (namaMatkul.isBlank()) {
                        errorMessage = "Nama mata kuliah tidak boleh kosong."
                        return@Button
                    }
                    if (jamMulai.isBlank() || jamSelesai.isBlank()) {
                        errorMessage = "Jam mulai dan jam selesai tidak boleh kosong."
                        return@Button
                    }

                    val timePattern = Regex("^([01][0-9]|2[0-3]):[0-5][0-9]$")
                    if (!timePattern.matches(jamMulai)) {
                        errorMessage = "Format jam mulai harus JJ:MM (contoh: 08:00)."
                        return@Button
                    }
                    if (!timePattern.matches(jamSelesai)) {
                        errorMessage = "Format jam selesai harus JJ:MM (contoh: 10:00)."
                        return@Button
                    }
                    if (jamSelesai <= jamMulai) {
                        errorMessage = "Jam selesai harus setelah jam mulai."
                        return@Button
                    }

                    val newItem = (initialData ?: Jadwal()).copy(
                        namaMatkul = namaMatkul.trim(),
                        hari = selectedHari,
                        jamMulai = jamMulai.trim(),
                        jamSelesai = jamSelesai.trim(),
                        ruangan = ruangan.trim(),
                        dosen = dosen.trim()
                    )
                    onSubmit(newItem)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(if (isEditMode) "Simpan Perubahan" else "Tambah Jadwal")
            }
        }
    }
}

/**
 * Membatasi dan memformat input waktu agar sesuai format JJ:MM (contoh 10:00).
 */
private fun formatTimeInput(input: String, previous: String): String {
    // Jika sedang menghapus karakter
    if (input.length < previous.length) {
        return if (previous.endsWith(":") && input.length == 2) {
            input.dropLast(1)
        } else {
            input
        }
    }

    val clean = input.filter { it.isDigit() }
    if (clean.isEmpty()) return ""

    val sb = StringBuilder()
    for (i in clean.indices) {
        if (sb.length >= 5) break
        val c = clean[i]
        when (i) {
            0 -> {
                if (c in '0'..'2') {
                    sb.append(c)
                } else if (c in '3'..'9') {
                    // Otomatis tambahkan 0 di depan jika mengetik angka 3-9
                    sb.append('0').append(c).append(':')
                }
            }
            1 -> {
                if (sb.length == 1) {
                    val first = sb[0]
                    if (first == '2') {
                        if (c in '0'..'3') {
                            sb.append(c).append(':')
                        }
                    } else {
                        sb.append(c).append(':')
                    }
                }
            }
            2 -> {
                if (c in '0'..'5') {
                    sb.append(c)
                }
            }
            3 -> {
                sb.append(c)
            }
        }
    }
    return sb.toString()
}
