package com.example.projekakhirpam.presentation.tugas

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.projekakhirpam.domain.model.TugasConst
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ---------- Helper tanggal / deadline ----------

private fun parser() = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply { isLenient = false }

fun isTanggalValid(teks: String): Boolean {
    if (!Regex("""\d{4}-\d{2}-\d{2}""").matches(teks)) return false
    return try { parser().parse(teks) != null } catch (e: Exception) { false }
}

/** Selisih hari dari hari ini ke deadline. Negatif berarti sudah lewat. */
fun sisaHari(deadline: String): Int? = try {
    val sdf = parser()
    val target = sdf.parse(deadline)
    val hariIni = sdf.parse(sdf.format(Date()))
    if (target == null || hariIni == null) null
    else Math.round((target.time - hariIni.time) / 86_400_000.0).toInt()
} catch (e: Exception) { null }

fun isTerlambat(deadline: String, status: String): Boolean =
    status != TugasConst.STATUS_COMPLETED && (sisaHari(deadline) ?: 0) < 0

fun labelDeadline(deadline: String, status: String): String {
    if (status == TugasConst.STATUS_COMPLETED) return "Selesai"
    val n = sisaHari(deadline) ?: return deadline
    return when {
        n < 0 -> "Terlambat ${-n} hari"
        n == 0 -> "Deadline hari ini"
        n == 1 -> "Deadline besok"
        else -> "$n hari lagi"
    }
}

fun formatTanggal(deadline: String): String = try {
    val tanggal = parser().parse(deadline)
    if (tanggal == null) deadline
    else SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag("id-ID")).format(tanggal)
} catch (e: Exception) { deadline }

// ---------- Komponen UI kecil ----------

@Composable
fun warnaPrioritas(prioritas: String): Pair<Color, Color> = when (prioritas) {
    TugasConst.PRIORITAS_HIGH ->
        MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
    TugasConst.PRIORITAS_MEDIUM ->
        MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
    else ->
        MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
}

@Composable
fun LabelKecil(teks: String, warnaLatar: Color, warnaTeks: Color) {
    Surface(color = warnaLatar, shape = RoundedCornerShape(50)) {
        Text(
            text = teks,
            color = warnaTeks,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}

/** Menampilkan pesan error dari ViewModel sebagai Toast. */
@Composable
fun PesanErrorToast(viewModel: TugasViewModel) {
    val pesan by viewModel.pesanError.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(pesan) {
        pesan?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }
}
