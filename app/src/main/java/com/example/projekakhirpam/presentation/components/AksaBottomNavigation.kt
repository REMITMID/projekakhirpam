package com.example.projekakhirpam.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

private data class Destination(val route: String, val label: String)

@Composable
fun AksaBottomNavigation(
    selectedRoute: String,
    onNavigate: (String) -> Unit
) {
    val destinations = listOf(
        Destination("home", "Home"),
        Destination("catatan_list", "Catatan Akademik"),
        Destination("tugas_list", "Tugas Kuliah"),
        Destination("more", "Lainnya")
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = CircleShape,
        color = Color(0xFF11121B),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
        destinations.forEach { destination ->
            val selected = selectedRoute == destination.route ||
                (selectedRoute in setOf("referensi_list", "aktivitas_list", "jadwal_list", "dosen_list") && destination.route == "more")
            val foreground = if (selected) Color(0xFF11121B) else Color.White
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .clickable { onNavigate(destination.route) }
                    .semantics { contentDescription = destination.label },
                contentAlignment = Alignment.Center
            ) {
                if (selected) {
                    Surface(shape = CircleShape, color = Color.White, modifier = Modifier.size(38.dp)) {}
                }
                when (destination.route) {
                    "home" -> Icon(
                        Icons.Default.Home,
                        contentDescription = null,
                        tint = foreground,
                        modifier = Modifier.size(21.dp)
                    )
                    "catatan_list", "tugas_list" -> AksaFeatureIcon(
                        route = destination.route,
                        tint = foreground,
                        modifier = Modifier.size(22.dp)
                    )
                    else -> MoreGlyph(foreground)
                }
            }
        }
        }
    }
}

@Composable
private fun MoreGlyph(color: Color) {
    Canvas(Modifier.size(20.dp)) {
        val dotRadius = size.minDimension * 0.11f
        val positions = listOf(0.3f to 0.3f, 0.7f to 0.3f, 0.3f to 0.7f, 0.7f to 0.7f)
        positions.forEach { (x, y) ->
            drawCircle(
                color = color,
                radius = dotRadius,
                center = androidx.compose.ui.geometry.Offset(size.width * x, size.height * y)
            )
        }
    }
}

