package com.example.projekakhirpam

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.projekakhirpam.domain.repository.AuthRepository
import com.example.projekakhirpam.presentation.aktivitas.*
import com.example.projekakhirpam.presentation.auth.*
import com.example.projekakhirpam.presentation.profile.ProfileScreen
import kotlinx.coroutines.runBlocking

@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    aktivitasViewModel: AktivitasViewModel,
    authRepository: AuthRepository
) {
    val currentUser = runBlocking { authRepository.getCurrentUser() }
    val startDestination = if (currentUser != null) "aktivitas_list" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = { navController.navigate("aktivitas_list") { popUpTo("login") { inclusive = true } } },
                onNavigateToSignUp = { navController.navigate("signup") }
            )
        }
        composable("signup") {
            SignUpScreen(
                viewModel = authViewModel,
                onSignUpSuccess = { navController.navigate("aktivitas_list") { popUpTo("signup") { inclusive = true } } },
                onNavigateToLogin = { navController.navigate("login") }
            )
        }
        composable("profile") {
            ProfileScreen(
                authRepository = authRepository,
                onLogout = { navController.navigate("login") { popUpTo(0) } },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("aktivitas_list") {
            AktivitasListScreen(
                viewModel = aktivitasViewModel,
                onAddClick = { navController.navigate("aktivitas_form") },
                onItemClick = { item -> navController.navigate("aktivitas_detail/${item.id}") },
                onProfileClick = { navController.navigate("profile") }
            )
        }
        composable("aktivitas_form") {
            AktivitasFormScreen(
                onSubmit = { item -> aktivitasViewModel.addAktivitas(item) { navController.popBackStack() } },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("aktivitas_detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            val state by aktivitasViewModel.uiState.collectAsState()
            if (state is AktivitasUiState.Success) {
                val item = (state as AktivitasUiState.Success).list.find { it.id == id }
                item?.let {
                    AktivitasDetailScreen(
                        item = it,
                        onEditClick = { navController.navigate("aktivitas_edit/${it.id}") },
                        onDeleteClick = { aktivitasViewModel.deleteAktivitas(it.id!!) { navController.popBackStack() } },
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
        composable("aktivitas_edit/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            val state by aktivitasViewModel.uiState.collectAsState()
            if (state is AktivitasUiState.Success) {
                val item = (state as AktivitasUiState.Success).list.find { it.id == id }
                item?.let {
                    AktivitasFormScreen(
                        initialData = it,
                        onSubmit = { updatedItem -> aktivitasViewModel.updateAktivitas(id!!, updatedItem) { navController.popBackStack() } },
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}