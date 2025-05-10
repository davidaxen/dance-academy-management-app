package com.daxen.mydancekmpsharedui.core.firebase.user.models

import kotlinx.serialization.Serializable

@Serializable
data class AcademyModel (
    var name: String = "",
    var nif: String = "",
    var address: String = "",
    var openingTime: String = "",
    var closingTime: String = "",
    var subscription: SubscriptionModel? = null,
)