package com.daxen.mydancekmpsharedui.features.auth.academy.ui.subscription

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.theme.BackgroundLight
import com.daxen.mydancekmpsharedui.features.auth.ui.components.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import kotlinx.coroutines.launch
import androidx.compose.foundation.Canvas
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding

@Composable
fun SubscriptionScreen(
    viewModel: SubscriptionViewModel,
    onNavigateNext: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val subscription by viewModel.subscription.collectAsState()
    val isSubmitting by viewModel.isSubmitting.collectAsState()

    val planDescriptions = mapOf(
        PlanType.STARTER to "Starter (Gratis)",
        PlanType.PRO to "Pro (29€/mes)",
        PlanType.ELITE to "Elite (desde 99€/mes)"
    )
    val planDetails = mapOf(
        PlanType.STARTER to listOf(
            "1 profesor incluido",
            "30 alumnos activos",
            "Reservas de alumnos",
            "Sin subida de vídeos",
            "Sin gestión de pagos",
            "Soporte email (48h)",
        ),
        PlanType.PRO to listOf(
            "5 profesores incluidos",
            "200 alumnos activos",
            "Reservas de alumnos",
            "Subida de vídeos (50GB)",
            "Gestión de pagos y bonos",
            "Soporte email (24h)",
        ),
        PlanType.ELITE to listOf(
            "Profesores ilimitados",
            "Alumnos ilimitados",
            "Reservas de alumnos",
            "Subida de vídeos (200GB+)",
            "Gestión de pagos y bonos",
            "Personalizacion (colores, diseño...)",
            "Soporte dedicado (chat o WhatsApp)",
        )
    )
    val availableAddons = listOf(
        AddOnType.EXTRA_PROFESSOR to "Profesor adicional (+2€/profesor)",
        AddOnType.EXTRA_ALUMNOS_50 to "Alumno adicional (bloque de 50) (+3€)",
        AddOnType.EXTRA_STORAGE_50GB to "Almacenamiento extra (50GB) (+5€)",
    )

    // Emojis e info visual para los planes
    val planEmojis = mapOf(
        PlanType.STARTER to "🎓",
        PlanType.PRO to "⭐",
        PlanType.ELITE to "👑"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        CurvedBackgroundFull()
        Column(modifier = Modifier.fillMaxSize()) {
            TopBarBackSection(onNavigateBack)

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AuthTitleAndSubtitle(
                    title = "Elige tu plan",
                    subtitle = "Selecciona el plan que mejor se adapte a tu academia",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // HorizontalPager de planes
                val plans = PlanType.entries.toTypedArray()
                val cardWidth: Dp = 340.dp
                val cardSpacing: Dp = 16.dp
                val listState = rememberLazyListState()
                val coroutineScope = rememberCoroutineScope()

                // Snap al card más cercano al terminar el scroll
                LaunchedEffect(listState.isScrollInProgress) {
                    if (!listState.isScrollInProgress) {
                        val item = (listState.firstVisibleItemScrollOffset > cardWidth.value / 2)
                            .let { offset ->
                                val base = listState.firstVisibleItemIndex
                                if (offset) base + 1 else base
                            }
                        coroutineScope.launch {
                            listState.animateScrollToItem(item.coerceIn(0, plans.lastIndex))
                        }
                    }
                }

                LazyRow(
                    state = listState,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(cardSpacing)
                ) {
                    itemsIndexed(plans) { _, plan ->
                        Box(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .fillParentMaxHeight(0.85f)
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxSize(),
                                shape = RoundedCornerShape(32.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 4.dp
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
//                                        .verticalScroll(rememberScrollState())
                                        .padding(bottom = LocalPadding.current.large, top = 0.dp, start = 0.dp, end = 0.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    // Header visual del plan
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.background)
                                            .padding(vertical = LocalPadding.current.tiny)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = planEmojis[plan] ?: "",
                                                fontSize = MaterialTheme.typography.displayMedium.fontSize,
                                                modifier = Modifier.padding(end = LocalPadding.current.tiny)
                                            )
                                            Text(
                                                text = planDescriptions[plan] ?: "",
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary,
                                                textAlign = TextAlign.Center,
                                            )
                                            if (plan == PlanType.PRO) {
                                                Box(
                                                    modifier = Modifier
                                                        .background(Color(0xFFFFD600), shape = RoundedCornerShape(8.dp))
                                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = "Más popular",
                                                        style = MaterialTheme.typography.labelMedium,
                                                        color = Color.Black
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Column(
                                        modifier = Modifier.padding(horizontal = 28.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        planDetails[plan]?.forEach {
                                            Text(
                                                text = "• $it",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                textAlign = TextAlign.Start,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(10.dp))
                                        HorizontalDivider(modifier = Modifier.padding(vertical = LocalPadding.current.extraTiny))
                                        Text(
                                            text = "Add-ons opcionales",
                                            style = MaterialTheme.typography.titleMedium,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        availableAddons.forEach { (addon, label) ->
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Checkbox(
                                                    checked = subscription.plan == plan && subscription.addons.contains(addon),
                                                    onCheckedChange = {
                                                        if (subscription.plan == plan) viewModel.toggleAddon(addon)
                                                    },
                                                    enabled = subscription.plan == plan
                                                )
                                                Text(
                                                    text = label,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    modifier = Modifier.padding(start = 8.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(24.dp))
                                    }
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .padding(vertical = LocalPadding.current.normal, horizontal = LocalPadding.current.big)
                            ) {
                                val priceText = "${calculatePrice(subscription.copy(plan = plan, addons = if (subscription.plan == plan) subscription.addons else emptyList()))} €/mes"
                                AuthButton(
                                    text = "Seleccionar plan ($priceText)",
                                    isLoading = isSubmitting && subscription.plan == plan,
                                    onClick = {
                                        viewModel.selectPlan(plan)
                                        onNavigateNext()
                                    },
                                    isDisabled = isSubmitting
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Indicadores de página (circulitos)
                val currentIndex = listState.firstVisibleItemIndex
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(bottom = 24.dp),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        plans.forEachIndexed { index, _ ->
                            val circleColor = if (index == currentIndex) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                            }
                            Canvas(
                                modifier = Modifier
                                    .size(22.dp)
                                    .padding(horizontal = 10.dp)
                            ) {
                                drawCircle(
                                    color = circleColor,
                                    radius = 14f
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}