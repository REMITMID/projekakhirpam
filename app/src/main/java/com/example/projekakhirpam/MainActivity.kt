package com.example.projekakhirpam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.projekakhirpam.core.network.SupabaseProvider
import com.example.projekakhirpam.data.repository.AktivitasRepositoryImpl
import com.example.projekakhirpam.data.repository.AuthRepositoryImpl
import com.example.projekakhirpam.presentation.aktivitas.AktivitasViewModel
import com.example.projekakhirpam.presentation.auth.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authRepository = AuthRepositoryImpl(SupabaseProvider.client)
        val aktivitasRepository = AktivitasRepositoryImpl(SupabaseProvider.client)

        val authViewModel = AuthViewModel(authRepository)
        val aktivitasViewModel = AktivitasViewModel(aktivitasRepository)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavigation(
                        navController = navController,
                        authViewModel = authViewModel,
                        aktivitasViewModel = aktivitasViewModel,
                        authRepository = authRepository
                    )
                }
            }
        }
    }
}