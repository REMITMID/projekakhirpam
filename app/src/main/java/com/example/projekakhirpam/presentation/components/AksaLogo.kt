package com.example.projekakhirpam.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.projekakhirpam.R

@Composable
fun AksaLogo(
    modifier: Modifier = Modifier,
    contentDescription: String = "Logo AKSA"
) {
    Image(
        painter = painterResource(R.drawable.aksa_logo),
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
        modifier = modifier
    )
}
