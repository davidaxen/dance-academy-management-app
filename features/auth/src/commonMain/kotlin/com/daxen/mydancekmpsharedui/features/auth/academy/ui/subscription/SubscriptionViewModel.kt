package com.daxen.mydancekmpsharedui.features.auth.academy.ui.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.user.model.AddOnType
import com.daxen.mydancekmpsharedui.data.user.model.PlanType
import com.daxen.mydancekmpsharedui.data.user.model.Subscription
import com.daxen.mydancekmpsharedui.data.user.repository.AcademyUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Modelos de datos
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
    private val academyUserRepository: AcademyUserRepository
): ViewModel() {
    private val _subscriptionState = MutableStateFlow<SubscriptionState>(SubscriptionState.Initial)
    val subscriptionState: StateFlow<SubscriptionState> = _subscriptionState.asStateFlow()

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
        viewModelScope.launch {
            try {
                _isSubmitting.value = true
                _subscriptionState.value = SubscriptionState.Loading
                academyUserRepository.setSubscription(_subscription.value)
                academyUserRepository.saveToDatabase()
                _subscriptionState.value = SubscriptionState.Success
            } catch (e: Exception) {
                println("Error al guardar la suscripción: $e")
            }
        }
    }

    fun onBackClicked() {
        _subscriptionState.value = SubscriptionState.Initial
        _isSubmitting.value = false
    }
}

sealed class SubscriptionState {
    data object Initial : SubscriptionState()
    data object Loading : SubscriptionState()
    data object Success : SubscriptionState()
    data class Error(val message: String) : SubscriptionState()
}