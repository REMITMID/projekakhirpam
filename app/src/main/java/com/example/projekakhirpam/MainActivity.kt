package com.example.projekakhirpam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.projekakhirpam.core.network.SupabaseProvider
import com.example.projekakhirpam.data.repository.AktivitasRepositoryImpl
import com.example.projekakhirpam.data.repository.AuthRepositoryImpl
import com.example.projekakhirpam.data.repository.DemoCatatanRepository
import com.example.projekakhirpam.data.repository.DemoDosenRepository
import com.example.projekakhirpam.data.repository.DemoReferensiRepository
import com.example.projekakhirpam.presentation.aktivitas.AktivitasViewModel
import com.example.projekakhirpam.presentation.auth.AuthViewModel
import com.example.projekakhirpam.presentation.koleksireferensi.ReferensiViewModel
import com.example.projekakhirpam.presentation.dosen.DosenViewModel
import com.example.projekakhirpam.presentation.theme.AksaTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val supabase = SupabaseProvider.client
        val authRepository = AuthRepositoryImpl(supabase)
        val aktivitasRepository = AktivitasRepositoryImpl(supabase)
        val referensiRepository = DemoReferensiRepository()
        val catatanRepository = DemoCatatanRepository()

        val authViewModel = AuthViewModel(authRepository)
        val aktivitasViewModel = AktivitasViewModel(aktivitasRepository)
        val referensiViewModel = ReferensiViewModel(referensiRepository)

        setContent {
            AksaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    AppNavigation(
                        navController = navController,
                        authViewModel = authViewModel,
                        aktivitasViewModel = aktivitasViewModel,
                        referensiViewModel = referensiViewModel,
                        authRepository = authRepository,
                        catatanRepository = catatanRepository,
                    )
                }
            }
        }
    }
}
