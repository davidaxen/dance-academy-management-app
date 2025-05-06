package com.daxen.mydancekmpsharedui.core.firebase.user.models

import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionModel(
    val plan: String = "",
    val addons: List<String> = emptyList()
)