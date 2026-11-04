package com.example.projekakhirpam.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.Image
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun aksaAuthTextFieldColors(): TextFieldColors {
    val colors = MaterialTheme.colorScheme
    return TextFieldDefaults.colors(
        focusedTextColor = colors.onSurface,
        unfocusedTextColor = colors.onSurface,
        disabledTextColor = colors.onSurfaceVariant,
        focusedContainerColor = colors.surfaceVariant,
        unfocusedContainerColor = colors.surfaceVariant,
        disabledContainerColor = colors.surfaceVariant,
        errorContainerColor = colors.surfaceVariant,
        cursorColor = colors.onSurface,
        errorCursorColor = colors.error,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = colors.error
    )
}

@Composable
fun AksaAuthLayout(
    topActionLabel: String,
    onTopAction: () -> Unit,
    onBack: () -> Unit,
    illustrationRes: Int,
    illustrationDescription: String,
    formContent: @Composable ColumnScope.() -> Unit
) {
    val colors = MaterialTheme.colorScheme

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        val headerHeight = (maxHeight * 0.42f).coerceIn(230.dp, 310.dp)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
                .background(colors.primary)
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(48.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = colors.onPrimary
                    )
                }
                TextButton(
                    onClick = onTopAction,
                    colors = ButtonDefaults.textButtonColors(contentColor = colors.onSurface),
                    modifier = Modifier.heightIn(min = 48.dp)
                ) {
                    Text(topActionLabel, style = MaterialTheme.typography.labelLarge)
                }
            }
            Image(
                painter = painterResource(illustrationRes),
                contentDescription = illustrationDescription,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = headerHeight - 26.dp)
                .imePadding(),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = colors.surface
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 520.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 26.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                content = formContent
            )
        }
    }
}
