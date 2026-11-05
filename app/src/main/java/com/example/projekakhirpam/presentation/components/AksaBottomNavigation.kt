package com.example.projekakhirpam.presentation.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import com.example.projekakhirpam.presentation.theme.AksaPalette
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.roundToInt
import kotlin.math.sin

private data class QuickDestination(val route: String, val title: String)

private val quickDestinations = listOf(
    QuickDestination("tugas_list", "Tugas Kuliah"),
    QuickDestination("jadwal_list", "Jadwal Kuliah"),
    QuickDestination("catatan_list", "Catatan Akademik"),
    QuickDestination("dosen_list", "Direktori Dosen"),
    QuickDestination("referensi_list", "Koleksi Referensi"),
    QuickDestination("aktivitas_list", "Aktivitas Kampus")
)

private fun radialAngle(index: Int): Double =
    PI * 0.82 - PI * 0.64 * index / (quickDestinations.size - 1)

/** Global navigation bar. The radial menu is an overlay and never changes the current route by itself. */
@Composable
fun AksaBottomNavigation(
    selectedRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val latestOnNavigate by rememberUpdatedState(onNavigate)
    var rootSize by remember { mutableStateOf(IntSize.Zero) }
    var serviceCenter by remember { mutableStateOf(Offset.Zero) }
    var menuTarget by remember { mutableStateOf(false) }
    var menuVisible by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(-1) }
    val progress = remember { Animatable(0f) }

    fun finishMenu(route: String? = null) {
        menuTarget = false
        scope.launch {
            progress.animateTo(0f, spring(dampingRatio = 0.82f, stiffness = 360f))
            menuVisible = false
            selectedIndex = -1
            route?.let(latestOnNavigate)
        }
    }

    BackHandler(enabled = menuVisible || menuTarget) {
        finishMenu()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { rootSize = IntSize(it.size.width, it.size.height) }
    ) {
        if (menuVisible || progress.value > 0f) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(AksaPalette.DeepBlack.copy(alpha = 0.48f * progress.value))
                    .clickable { finishMenu() }
            )
        }

        val widthDp = with(density) { rootSize.width.toDp() }
        val heightDp = with(density) { rootSize.height.toDp() }
        val center = with(density) { Offset(serviceCenter.x, serviceCenter.y) }
        val radius = minOf(180.dp, widthDp / 2f - 34.dp, heightDp - 150.dp).coerceAtLeast(72.dp)
        if (menuVisible || progress.value > 0f) {
            quickDestinations.forEachIndexed { index, destination ->
                val angle = radialAngle(index)
                val x = center.x + with(density) { radius.toPx() } * cos(angle).toFloat() * progress.value
                val y = center.y - with(density) { radius.toPx() } * sin(angle).toFloat() * progress.value
                val highlighted = selectedIndex == index
                val iconScale by animateFloatAsState(
                    targetValue = if (highlighted) 1.14f else 1f,
                    animationSpec = spring(stiffness = 420f),
                    label = "radialItemScale$index"
                )
                Surface(
                    modifier = Modifier
                        .offset { IntOffset((x - with(density) { 27.dp.toPx() }).roundToInt(), (y - with(density) { 27.dp.toPx() }).roundToInt()) }
                        .size(54.dp)
                        .scale(iconScale)
                        .clickable { finishMenu(destination.route) }
                        .semantics { contentDescription = destination.title },
                    shape = CircleShape,
                    color = if (highlighted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    shadowElevation = 8.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        AksaFeatureIcon(
                            route = destination.route,
                            tint = if (highlighted) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
            }

            if (selectedIndex in quickDestinations.indices) {
                val destination = quickDestinations[selectedIndex]
                val angle = radialAngle(selectedIndex)
                val pxRadius = with(density) { radius.toPx() }
                val x = serviceCenter.x + pxRadius * cos(angle).toFloat() * progress.value
                val y = serviceCenter.y - pxRadius * sin(angle).toFloat() * progress.value
                Layout(
                    content = { RadialFeatureLabel(destination) },
                    modifier = Modifier.fillMaxSize(),
                    measurePolicy = { measurables, constraints ->
                    val edgeInset = with(density) { 12.dp.roundToPx() }
                    val maxBubbleWidth = minOf(
                        constraints.maxWidth - edgeInset * 2,
                        with(density) { 220.dp.roundToPx() }
                    ).coerceAtLeast(1)
                    val placeable = measurables.single().measure(
                        Constraints(
                            maxWidth = maxBubbleWidth,
                            maxHeight = with(density) { 64.dp.roundToPx() }
                        )
                    )
                    val left = (x - placeable.width / 2f)
                        .roundToInt()
                        .coerceIn(edgeInset, (constraints.maxWidth - placeable.width - edgeInset).coerceAtLeast(edgeInset))
                    val top = (y - with(density) { 27.dp.toPx() } - placeable.height - with(density) { 5.dp.toPx() })
                        .roundToInt()
                        .coerceIn(edgeInset, (constraints.maxHeight - placeable.height - edgeInset).coerceAtLeast(edgeInset))

                    layout(constraints.maxWidth, constraints.maxHeight) {
                        placeable.placeRelative(left, top)
                    }
                    }
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .fillMaxWidth()
                .height(88.dp)
        ) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(70.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 10.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                NavigationItem(
                    label = "Home",
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    selected = selectedRoute == "home",
                    onClick = { if (menuVisible || menuTarget) finishMenu("home") else latestOnNavigate("home") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.width(60.dp))

                NavigationItem(
                    label = "Profil",
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    selected = selectedRoute == "profile",
                    onClick = { if (menuVisible || menuTarget) finishMenu("profile") else latestOnNavigate("profile") },
                    modifier = Modifier.weight(1f)
                )
                }
            }

            // Keep the center action outside the rounded navigation surface so its shadow and circle
            // remain fully visible instead of being clipped by the pill-shaped bar.
            Surface(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .size(60.dp)
                        .then(
                            if (selectedRoute == "more") Modifier.border(
                                width = 3.dp,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = CircleShape
                            ) else Modifier
                        )
                        .onGloballyPositioned {
                            serviceCenter = it.positionInRoot() + Offset(it.size.width / 2f, it.size.height / 2f)
                        }
                        .pointerInput(Unit) {
                            awaitEachGesture {
                                val down = awaitFirstDown(requireUnconsumed = false)
                                val downPosition = down.position
                                val pointerId = down.id
                                var releasedBeforeLongPress = false
                                val longPress = withTimeoutOrNull(viewConfiguration.longPressTimeoutMillis) {
                                    while (true) {
                                        val event = awaitPointerEvent(PointerEventPass.Main)
                                        val change = event.changes.firstOrNull { it.id == pointerId } ?: continue
                                        if (change.changedToUp()) {
                                            releasedBeforeLongPress = true
                                            return@withTimeoutOrNull false
                                        }
                                        if ((change.position - downPosition).getDistance() > viewConfiguration.touchSlop) {
                                            return@withTimeoutOrNull false
                                        }
                                    }
                                    @Suppress("UNREACHABLE_CODE")
                                    true
                                } ?: true

                                if (!longPress) {
                                    if (releasedBeforeLongPress) latestOnNavigate("more")
                                    return@awaitEachGesture
                                }

                                down.consume()
                                menuVisible = true
                                menuTarget = true
                                selectedIndex = -1
                                scope.launch {
                                    progress.animateTo(1f, spring(dampingRatio = 0.82f, stiffness = 360f))
                                }
                                var chosen = -1
                                var finished = false
                                while (!finished) {
                                    val event = awaitPointerEvent(PointerEventPass.Main)
                                    val change = event.changes.firstOrNull { it.id == pointerId } ?: break
                                    val rootPoint = serviceCenter + (change.position - downPosition)
                                    val pxRadius = with(density) { radius.toPx() } * progress.value
                                    // Hit-test against the icons' animated positions, not their final
                                    // positions, so the finger follows what is actually on screen.
                                    chosen = if (progress.value >= 0.4f) {
                                        quickDestinations.indices.minByOrNull { index ->
                                            val angle = radialAngle(index)
                                            val point = Offset(
                                                serviceCenter.x + pxRadius * cos(angle).toFloat(),
                                                serviceCenter.y - pxRadius * sin(angle).toFloat()
                                            )
                                            hypot(rootPoint.x - point.x, rootPoint.y - point.y)
                                        }?.takeIf { index ->
                                            val angle = radialAngle(index)
                                            val point = Offset(
                                                serviceCenter.x + pxRadius * cos(angle).toFloat(),
                                                serviceCenter.y - pxRadius * sin(angle).toFloat()
                                            )
                                            hypot(rootPoint.x - point.x, rootPoint.y - point.y) <= with(density) { 34.dp.toPx() }
                                        } ?: -1
                                    } else {
                                        -1
                                    }
                                    selectedIndex = chosen
                                    change.consume()
                                    finished = !change.pressed
                                }

                                finishMenu(chosen.takeIf { it >= 0 }?.let { quickDestinations[it].route })
                            }
                        }
                        .semantics {
                            contentDescription = "Layanan, ketuk untuk membuka semua fitur, tekan tahan untuk navigasi cepat"
                            selected = selectedRoute == "more"
                        },
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    shadowElevation = 8.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Apps,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
        }
    }
}

@Composable
private fun RadialFeatureLabel(destination: QuickDestination) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            shadowElevation = 5.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AksaFeatureIcon(
                    route = destination.route,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = destination.title,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun NavigationItem(
    label: String,
    icon: @Composable () -> Unit,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
        modifier = modifier.semantics { this.selected = selected }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp)) {
            CompositionLocalProvider(LocalContentColor provides if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant) {
                Surface(
                    shape = CircleShape,
                    color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) { icon() }
                }
                Text(label, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
