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
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalDensity
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.data.user.model.PlanType
import com.daxen.mydancekmpsharedui.features.auth.academy.ui.subscription.utils.Constants.availableAddons
import com.daxen.mydancekmpsharedui.features.auth.academy.ui.subscription.utils.Constants.planDescriptions
import com.daxen.mydancekmpsharedui.features.auth.academy.ui.subscription.utils.Constants.planDetails
import com.daxen.mydancekmpsharedui.features.auth.academy.ui.subscription.utils.Constants.planEmojis

@Composable
fun SubscriptionScreen(
    viewModel: SubscriptionViewModel,
    onNavigateNext: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val subscription by viewModel.subscription.collectAsState()
    val isSubmitting by viewModel.isSubmitting.collectAsState()
    val state by viewModel.subscriptionState.collectAsState()
    var isNavigating by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state is SubscriptionState.Success && !isNavigating) {
            isNavigating = true
            onNavigateNext()
            viewModel.onBackClicked()
        }
    }

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
                val density = LocalDensity.current
                // Snap al card más cercano al terminar el scroll, pero solo de uno en uno
                LaunchedEffect(listState.isScrollInProgress) {
                    if (!listState.isScrollInProgress) {
                        val offsetPx = listState.firstVisibleItemScrollOffset
                        val cardPx = with(density) { cardWidth.toPx() }
                        val current = listState.firstVisibleItemIndex
                        val direction = if (offsetPx > cardPx / 4) 1 else if (offsetPx < -cardPx / 4) -1 else 0
                        val target = (current + direction).coerceIn(0, plans.lastIndex)
                        coroutineScope.launch {
                            listState.animateScrollToItem(target)
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
                                            val checked = subscription.plan == plan && subscription.addons.contains(addon)
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 2.dp)
                                                    .let {
                                                        if (subscription.plan == plan) it.clickable { viewModel.toggleAddon(addon) } else it
                                                    },
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Checkbox(
                                                    checked = checked,
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
                                        viewModel.submitSubscription()
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