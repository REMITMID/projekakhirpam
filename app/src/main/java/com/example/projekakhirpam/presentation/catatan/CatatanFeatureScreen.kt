package com.example.projekakhirpam.presentation.catatan

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.projekakhirpam.domain.repository.CatatanRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatatanFeatureScreen(
    repository: CatatanRepository
) {

    // Inisialisasi ViewModel
    val vm: CatatanViewModel = viewModel(
        factory = remember(repository) {
            object : ViewModelProvider.Factory {

                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>
                ): T {
                    return CatatanViewModel(repository) as T
                }
            }
        }
    )

    val state by vm.ui.collectAsState()

    // State navigasi lokal fitur
    var page by rememberSaveable {
        mutableStateOf("semester")
    }

    var mataKuliahId by rememberSaveable {
        mutableStateOf("")
    }

    var mataKuliahNama by rememberSaveable {
        mutableStateOf("")
    }

    var pertemuanId by rememberSaveable {
        mutableStateOf("")
    }

    var pertemuanNomor by rememberSaveable {
        mutableIntStateOf(0)
    }

    var catatanEditId by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var judul by rememberSaveable {
        mutableStateOf("")
    }

    var isi by rememberSaveable {
        mutableStateOf("")
    }

    var dialog by remember {
        mutableStateOf("")
    }

    var input by remember {
        mutableStateOf("")
    }

    var hapusId by remember {
        mutableStateOf("")
    }

    // Memuat mata kuliah semester awal
    LaunchedEffect(Unit) {
        vm.pilihSemester(1)
    }

    // Navigasi kembali
    fun kembali() {
        page = when (page) {
            "matkul" -> "semester"
            "pertemuan" -> "matkul"
            "catatan" -> "pertemuan"
            "form" -> "catatan"
            else -> "semester"
        }
    }

    BackHandler(page != "semester") {
        kembali()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (page) {
                            "semester" -> "Catatan Akademik"
                            "matkul" -> "Semester ${state.semester}"
                            "pertemuan" -> mataKuliahNama
                            "catatan" -> "Pertemuan $pertemuanNomor"
                            else -> {
                                if (catatanEditId == null)
                                    "Tambah Catatan"
                                else
                                    "Edit Catatan"
                            }
                        }
                    )
                },
                navigationIcon = {
                    if (page != "semester") {
                        IconButton(onClick = { kembali() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Kembali"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            if (state.loading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }

            state.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )

                TextButton(
                    onClick = { vm.bersihkanError() }
                ) {
                    Text("Tutup pesan")
                }
            }

            when (page) {

                // =========================
                // HALAMAN SEMESTER
                // =========================

                "semester" -> {

                    Text(
                        "Pilih semester",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(12.dp))

                    var expanded by remember {
                        mutableStateOf(false)
                    }

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            expanded = it
                        }
                    ) {

                        OutlinedTextField(
                            value = "Semester ${state.semester}",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Semester") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults
                                    .TrailingIcon(expanded)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            }
                        ) {
                            (1..8).forEach { semester ->

                                DropdownMenuItem(
                                    text = {
                                        Text("Semester $semester")
                                    },
                                    onClick = {
                                        expanded = false
                                        vm.pilihSemester(semester)
                                    }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = {
                            page = "matkul"
                        },
                        enabled = !state.loading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Lihat Mata Kuliah")
                    }
                }

                // =========================
                // HALAMAN MATA KULIAH
                // =========================

                "matkul" -> {

                    Button(
                        onClick = {
                            input = ""
                            dialog = "tambah_matkul"
                        }
                    ) {
                        Text("+ Tambah Mata Kuliah")
                    }

                    Spacer(Modifier.height(12.dp))

                    if (state.mataKuliah.isEmpty()) {
                        EmptyMessage(
                            "Belum ada mata kuliah pada semester ini"
                        )
                    }

                    LazyColumn(
                        verticalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {

                        items(
                            state.mataKuliah,
                            key = { it.id }
                        ) { mk ->

                            CatatanItemCard(
                                title = mk.nama,

                                onClick = {
                                    mataKuliahId = mk.id
                                    mataKuliahNama = mk.nama

                                    vm.bukaMataKuliah(mk.id)
                                    page = "pertemuan"
                                },

                                onDelete = {
                                    hapusId = mk.id
                                    dialog = "hapus_matkul"
                                }
                            )
                        }
                    }
                }

                // =========================
                // HALAMAN PERTEMUAN
                // =========================

                "pertemuan" -> {

                    Button(
                        onClick = {
                            input = ""
                            dialog = "tambah_pertemuan"
                        }
                    ) {
                        Text("+ Tambah Pertemuan")
                    }

                    Spacer(Modifier.height(12.dp))

                    if (state.pertemuan.isEmpty()) {
                        EmptyMessage(
                            "Belum ada pertemuan. " +
                                "Tambahkan pertemuan pertama."
                        )
                    }

                    LazyColumn(
                        verticalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {

                        items(
                            state.pertemuan,
                            key = { it.id }
                        ) { pertemuan ->

                            CatatanItemCard(
                                title =
                                    "Pertemuan ${pertemuan.nomor}",

                                onClick = {
                                    pertemuanId = pertemuan.id
                                    pertemuanNomor = pertemuan.nomor

                                    vm.bukaPertemuan(
                                        pertemuan.id
                                    )

                                    page = "catatan"
                                },

                                onDelete = {
                                    hapusId = pertemuan.id
                                    dialog = "hapus_pertemuan"
                                }
                            )
                        }
                    }
                }

                // =========================
                // DAFTAR CATATAN
                // =========================

                "catatan" -> {

                    Button(
                        onClick = {
                            catatanEditId = null
                            judul = ""
                            isi = ""
                            page = "form"
                        }
                    ) {
                        Text("+ Tambah Catatan")
                    }

                    Spacer(Modifier.height(12.dp))

                    if (state.catatan.isEmpty()) {
                        EmptyMessage(
                            "Belum ada catatan pada pertemuan ini"
                        )
                    }

                    LazyColumn(
                        verticalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {

                        items(
                            state.catatan,
                            key = { it.id }
                        ) { catatan ->

                            CatatanItemCard(
                                title = catatan.judul,
                                subtitle = catatan.isi,

                                onClick = {
                                    catatanEditId = catatan.id
                                    judul = catatan.judul
                                    isi = catatan.isi
                                    page = "form"
                                },

                                onDelete = {
                                    hapusId = catatan.id
                                    dialog = "hapus_catatan"
                                }
                            )
                        }
                    }
                }

                // =========================
                // FORM TAMBAH / EDIT
                // =========================

                "form" -> {

                    OutlinedTextField(
                        value = judul,
                        onValueChange = {
                            judul = it
                        },
                        label = {
                            Text("Judul Catatan")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = isi,
                        onValueChange = {
                            isi = it
                        },
                        label = {
                            Text("Isi Catatan")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        minLines = 6
                    )

                    Spacer(Modifier.height(12.dp))

                    Button(
                        enabled = !state.loading &&
                            judul.isNotBlank() &&
                            isi.isNotBlank(),

                        onClick = {
                            vm.simpanCatatan(
                                pertemuanId = pertemuanId,
                                id = catatanEditId,
                                judul = judul,
                                isi = isi
                            ) {
                                page = "catatan"
                            }
                        },

                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Simpan Catatan")
                    }
                }
            }
        }
    }

    // =========================
    // DIALOG TAMBAH DAN HAPUS
    // =========================

    if (dialog.isNotEmpty()) {

        val kind = dialog
        val isTambah = kind.startsWith("tambah")

        AlertDialog(
            onDismissRequest = {
                dialog = ""
            },

            title = {
                Text(
                    when (kind) {
                        "tambah_matkul" ->
                            "Tambah Mata Kuliah"

                        "tambah_pertemuan" ->
                            "Tambah Pertemuan"

                        else ->
                            "Konfirmasi Hapus"
                    }
                )
            },

            text = {
                if (isTambah) {

                    OutlinedTextField(
                        value = input,
                        onValueChange = {
                            input = it
                        },
                        label = {
                            Text(
                                if (kind == "tambah_matkul")
                                    "Nama Mata Kuliah"
                                else
                                    "Nomor Pertemuan"
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType =
                                if (kind == "tambah_pertemuan")
                                    KeyboardType.Number
                                else
                                    KeyboardType.Text
                        ),
                        singleLine = true
                    )

                } else {

                    Text(
                        "Data ini dan seluruh data " +
                            "di bawahnya akan terhapus permanen. " +
                            "Lanjutkan?"
                    )
                }
            },

            confirmButton = {

                TextButton(
                    enabled = !state.loading,
                    onClick = {

                        when (kind) {

                            "tambah_matkul" -> {
                                vm.tambahMataKuliah(input) {
                                    dialog = ""
                                }
                            }

                            "tambah_pertemuan" -> {
                                vm.tambahPertemuan(
                                    mataKuliahId,
                                    input.toIntOrNull() ?: 0
                                ) {
                                    dialog = ""
                                }
                            }

                            "hapus_matkul" -> {
                                vm.hapusMataKuliah(hapusId) {
                                    dialog = ""
                                }
                            }

                            "hapus_pertemuan" -> {
                                vm.hapusPertemuan(
                                    mataKuliahId,
                                    hapusId
                                ) {
                                    dialog = ""
                                }
                            }

                            "hapus_catatan" -> {
                                vm.hapusCatatan(
                                    pertemuanId,
                                    hapusId
                                ) {
                                    dialog = ""
                                }
                            }
                        }
                    }
                ) {
                    Text(
                        if (isTambah) "Tambah" else "Hapus"
                    )
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        dialog = ""
                    }
                ) {
                    Text("Batal")
                }
            }
        )
    }
}

// =========================
// KOMPONEN PESAN KOSONG
// =========================

@Composable
private fun EmptyMessage(message: String) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),

        contentAlignment = Alignment.Center
    ) {
        Text(
            message,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// =========================
// KOMPONEN KARTU DATA
// =========================

@Composable
private fun CatatanItemCard(
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {

    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onClick)
            ) {

                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium
                )

                subtitle?.let {
                    Text(
                        text = it,
                        maxLines = 2,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Text(
                    "Ketuk untuk membuka",
                    style = MaterialTheme.typography.labelSmall
                )
            }

            TextButton(
                onClick = onDelete
            ) {
                Text("Hapus")
            }
        }
    }
}