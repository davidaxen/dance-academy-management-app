package com.daxen.mydancekmpsharedui.core.firebase.user.models

import kotlinx.serialization.Serializable

@Serializable
data class AcademyDetailsModel(
    val name: String = "",
    val address: String = "",
    val nif: String = "",
    val openingTime: String = "",
    val closingTime: String = "",
    val imageUrl: String = "",
    val ownerId: String = ""
) 