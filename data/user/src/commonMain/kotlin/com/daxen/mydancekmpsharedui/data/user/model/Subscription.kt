package com.daxen.mydancekmpsharedui.data.user.model

data class Subscription(
    val plan: PlanType,
    val addons: List<AddOnType>
)