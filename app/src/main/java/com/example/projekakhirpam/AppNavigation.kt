package com.example.projekakhirpam

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.projekakhirpam.domain.repository.AuthRepository
import com.example.projekakhirpam.domain.repository.CatatanRepository
import com.example.projekakhirpam.presentation.aktivitas.*
import com.example.projekakhirpam.presentation.auth.*
import com.example.projekakhirpam.presentation.catatan.CatatanFeatureScreen
import com.example.projekakhirpam.presentation.catatan.RecentCatatanStore
import com.example.projekakhirpam.presentation.home.HomeScreen
import com.example.projekakhirpam.presentation.home.MoreFeaturesScreen
import com.example.projekakhirpam.presentation.koleksireferensi.ReferensiDetailScreen
import com.example.projekakhirpam.presentation.koleksireferensi.ReferensiFormScreen
import com.example.projekakhirpam.presentation.koleksireferensi.ReferensiListScreen
import com.example.projekakhirpam.presentation.koleksireferensi.ReferensiViewModel
import com.example.projekakhirpam.presentation.profile.ProfileScreen
import com.example.projekakhirpam.presentation.components.AksaBottomNavigation

@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    aktivitasViewModel: AktivitasViewModel,
    referensiViewModel: ReferensiViewModel,
    authRepository: AuthRepository,
    catatanRepository: CatatanRepository
) {
    var isLoading by remember { mutableStateOf(true) }
    var startDestination by remember { mutableStateOf("welcome") }

    LaunchedEffect(Unit) {
        val currentUser = authRepository.getCurrentUser()
        // Jika sudah login, langsung buka HALAMAN UTAMA (home)
        startDestination = if (currentUser != null) "home" else "welcome"
        isLoading = false
    }

    fun navigateToMain(route: String) {
        navController.navigate(route) {
            // Main tabs open their root destination. In particular, Layanan must
            // show the feature grid instead of restoring its last opened feature.
            popUpTo("home") { inclusive = false }
            launchSingleTop = true
        }
    }

    fun navigateToFeature(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
            restoreState = true
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route.orEmpty()
        val showMainNavigation = currentRoute !in setOf("splash", "welcome", "login", "signup")

        Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.fillMaxSize().padding(bottom = if (showMainNavigation) 122.dp else 0.dp)
        ) {

            // --- AUTH & PROFILE ---
            composable("welcome") {
                WelcomeScreen(
                    onSignIn = {
                        navController.navigate("login") {
                            launchSingleTop = true
                        }
                    },
                    onSignUp = {
                        navController.navigate("signup") {
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable("login") {
                LoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = {
                        aktivitasViewModel.loadAktivitas()
                        referensiViewModel.loadReferensi()
                        navController.navigate("home") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    },
                    onNavigateToSignUp = {
                        navController.navigate("signup") { launchSingleTop = true }
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable("signup") {
                SignUpScreen(
                    viewModel = authViewModel,
                    onSignUpSuccess = {
                        aktivitasViewModel.loadAktivitas()
                        referensiViewModel.loadReferensi()
                        navController.navigate("home") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigate("login") {
                            popUpTo("signup") { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable("profile") {
                ProfileScreen(
                    authRepository = authRepository,
                    onLogout = {
                        authViewModel.resetAuthState()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onBackClick = { navController.popBackStack() },
                    onNavigateRoute = { route -> navigateToMain(route) }
                )
            }

            // --- HALAMAN UTAMA (DASHBOARD 6 MENU) ---
            composable("home") {
                HomeScreen(
                    onOpenCatatan = { navigateToFeature("catatan_list") },
                    onOpenRecentNote = {
                        RecentCatatanStore.requestOpenRecent()
                        navigateToFeature("catatan_list")
                    },
                    onProfileClick = { navigateToMain("profile") }
                )
            }
            composable("more") {
                MoreFeaturesScreen(
                    onNavigateRoute = { route -> navigateToFeature(route) },
                    onProfileClick = { navigateToMain("profile") }
                )
            }

            // --- FITUR 5: KOLEKSI REFERENSI (FITURMU) ---
            composable("referensi_list") {
                ReferensiListScreen(
                    viewModel = referensiViewModel,
                    onNavigateRoute = { route -> navigateToMain(route) },
                    onNavigateToDetail = { referensiId ->
                        navController.navigate("referensi_detail/$referensiId")
                    },
                    onNavigateToAdd = {
                        navController.navigate("referensi_form")
                    }
                )
            }
            composable("referensi_detail/{id}") { backStackEntry ->
                val idString = backStackEntry.arguments?.getString("id")
                val referensiId = idString?.toIntOrNull() ?: 0

                ReferensiDetailScreen(
                    referensiId = referensiId,
                    viewModel = referensiViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToEdit = { editId ->
                        navController.navigate("referensi_form?id=$editId")
                    }
                )
            }
            composable("referensi_form?id={id}") { backStackEntry ->
                val idString = backStackEntry.arguments?.getString("id")
                val referensiId = idString?.toIntOrNull()

                ReferensiFormScreen(
                    referensiId = referensiId,
                    viewModel = referensiViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // --- FITUR CATATAN AKADEMIK ---
            composable("catatan_list") {
                CatatanFeatureScreen(
                    repository = catatanRepository,
                    onNavigateRoute = { route -> navigateToMain(route) }
                )
            }

            // --- FITUR AKTIVITAS KAMPUS ---
            composable("aktivitas_list") {
                AktivitasListScreen(
                    viewModel = aktivitasViewModel,
                    onAddClick = { navController.navigate("aktivitas_form") },
                    onItemClick = { item -> navController.navigate("aktivitas_detail/${item.id}") },
                    onProfileClick = { navigateToMain("profile") },
                    onNavigateRoute = { route -> navigateToMain(route) }
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
                            onDeleteClick = {
                                it.id?.let { itemId ->
                                    aktivitasViewModel.deleteAktivitas(itemId) { navController.popBackStack() }
                                }
                            },
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
                            onSubmit = { updatedItem ->
                                id?.let { itemId ->
                                    aktivitasViewModel.updateAktivitas(itemId, updatedItem) { navController.popBackStack() }
                                }
                            },
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }
            }

            // --- PLACEHOLDER FITUR TEMAN KELOMPOK LAIN (FITUR 1-4) ---
            composable("tugas_list") { PlaceholderScreen("Manajemen Tugas Kuliah") }
            composable("jadwal_list") { PlaceholderScreen("Jadwal Perkuliahan") }
            composable("dosen_list") { PlaceholderScreen("Direktori Dosen") }
        }
            if (showMainNavigation) {
                AksaBottomNavigation(
                    selectedRoute = when (currentRoute) {
                        "home" -> "home"
                        "profile" -> "profile"
                        else -> "more"
                    },
                    onNavigate = { route ->
                        when (route) {
                            "home", "profile", "more" -> navigateToMain(route)
                            else -> {
                                navController.navigate(route) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Halaman $title sedang dikembangkan oleh anggota kelompok.")
    }
}
