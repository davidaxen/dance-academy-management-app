package com.daxen.mydancekmpsharedui.core.firebase.classes.response

import kotlinx.serialization.Serializable

@Serializable
data class SpecificClassesResponse(
    val date: String,
    override val id: String,
    override val name: String,
    override val hour: String,
    override val status: String,
    override val teacherId: String,
): BaseClassResponse