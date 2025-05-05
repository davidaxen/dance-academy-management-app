package com.daxen.mydancekmpsharedui.features.auth.academy.ui.subscription

import androidx.lifecycle.ViewModel
import com.daxen.mydancekmpsharedui.data.auth.repository.AuthRepository
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Modelos de datos
data class Subscription(
    val plan: PlanType,
    val addons: List<AddOnType>
)

enum class PlanType { STARTER, PRO, ELITE }

enum class AddOnType {
    EXTRA_PROFESSOR,
    EXTRA_ALUMNOS_50,
    EXTRA_STORAGE_50GB,
}

fun calculatePrice(subscription: Subscription): Int {
    val basePrice = when (subscription.plan) {
        PlanType.STARTER -> 0
        PlanType.PRO -> 29
        PlanType.ELITE -> 99
    }

    val addonsPrice = subscription.addons.map { addon ->
        when (addon) {
            AddOnType.EXTRA_PROFESSOR -> 2
            AddOnType.EXTRA_ALUMNOS_50 -> 3
            AddOnType.EXTRA_STORAGE_50GB -> 5
        }
    }.sum()

    return basePrice + addonsPrice
}

class SubscriptionViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
): ViewModel() {
    private val _subscription = MutableStateFlow(
        Subscription(plan = PlanType.STARTER, addons = emptyList())
    )
    val subscription: StateFlow<Subscription> = _subscription.asStateFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    fun selectPlan(plan: PlanType) {
        _subscription.value = _subscription.value.copy(plan = plan, addons = emptyList())
    }

    fun toggleAddon(addon: AddOnType) {
        val currentAddons = _subscription.value.addons.toMutableList()
        if (currentAddons.contains(addon)) {
            currentAddons.remove(addon)
        } else {
            currentAddons.add(addon)
        }
        _subscription.value = _subscription.value.copy(addons = currentAddons)
    }

    fun submitSubscription() {
        // Aquí iría la lógica para guardar la suscripción
        // Por ahora, solo simula éxito
        _isSubmitting.value = true
        try {
        } catch (e: Exception) {
            println("Error al guardar la suscripción: $e")
        } finally {
            _isSubmitting.value = false
        }
    }
}