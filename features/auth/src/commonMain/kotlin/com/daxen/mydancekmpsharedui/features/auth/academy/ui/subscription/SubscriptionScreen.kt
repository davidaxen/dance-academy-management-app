package com.daxen.mydancekmpsharedui.features.auth.academy.ui.subscription

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.core.ui.theme.BackgroundLight
import com.daxen.mydancekmpsharedui.features.auth.ui.components.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.launch

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
        PlanType.ELITE to "Elite (Marca Blanca – desde 99€/mes)"
    )
    val planDetails = mapOf(
        PlanType.STARTER to listOf(
            "1 profesor incluido",
            "30 alumnos activos",
            "Reservas básicas",
            "Sin subida de vídeos",
            "Sin gestión de pagos",
            "Branding: logo",
            "Estadísticas básicas",
            "Soporte email (72h)",
            "Sin app separada"
        ),
        PlanType.PRO to listOf(
            "5 profesores incluidos",
            "200 alumnos activos",
            "Reservas avanzadas",
            "Subida de vídeos (50GB)",
            "Gestión de pagos y paquetes",
            "Branding parcial",
            "Estadísticas detalladas",
            "Soporte email (24h)",
            "Sin app separada"
        ),
        PlanType.ELITE to listOf(
            "Profesores ilimitados",
            "Alumnos ilimitados",
            "Reservas avanzadas + Marca Blanca",
            "Subida de vídeos (200GB+)",
            "Gestión de pagos y paquetes",
            "Branding completo (colores, dominio, app propia)",
            "Estadísticas premium",
            "Soporte dedicado (chat o WhatsApp)",
            "App separada (publicada en su cuenta)"
        )
    )
    val availableAddons = listOf(
        AddOnType.EXTRA_PROFESSOR to "Profesor adicional (+2€/profesor)",
        AddOnType.EXTRA_ALUMNOS_50 to "Alumno adicional (bloque de 50) (+3€)",
        AddOnType.EXTRA_STORAGE_50GB to "Almacenamiento extra (50GB) (+5€)",
        AddOnType.CUSTOM_DOMAIN to "Dominio personalizado (+4€)",
        AddOnType.APPSTORE_PUBLISH to "Publicación en App Store (iOS) (+10€)",
        AddOnType.REMOVE_BRANDING to "Eliminación del branding 'by tuapp' (+10€)"
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
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AuthTitleAndSubtitle(
                    title = "Elige tu plan",
                    subtitle = "Selecciona el plan que mejor se adapte a tu academia",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // HorizontalPager de planes
                val plans = PlanType.values()
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
                    itemsIndexed(plans) { index, plan ->
                        val isSelected = subscription.plan == plan
                        Card(
                            modifier = Modifier
                                .width(cardWidth)
                                .height(540.dp),
                            shape = RoundedCornerShape(32.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 4.dp
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(28.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = planDescriptions[plan] ?: "",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                planDetails[plan]?.forEach {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                                if (plan != PlanType.STARTER) {
                                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                                    Text(
                                        text = "Add-ons opcionales",
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    availableAddons.forEach { (addon, label) ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Checkbox(
                                                checked = isSelected && subscription.addons.contains(addon),
                                                onCheckedChange = {
                                                    if (isSelected) viewModel.toggleAddon(addon)
                                                },
                                                enabled = isSelected
                                            )
                                            Text(
                                                text = label,
                                                style = MaterialTheme.typography.bodyMedium,
                                                modifier = Modifier.padding(start = 8.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                                Text(
                                    text = "Precio total: ${calculatePrice(subscription.copy(plan = plan, addons = if (isSelected) subscription.addons else emptyList()))} €/mes",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                AuthButton(
                                    text = "Seleccionar plan",
                                    isLoading = isSubmitting && isSelected,
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
            }
        }
    }
}

// Extension para convertir dp a px
@Composable
private fun Float.dpToPx(): Float {
    val density = LocalDensity.current
    return with(density) { this@dpToPx.dp.toPx() }
}