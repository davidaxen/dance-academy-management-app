package com.daxen.mydancekmpsharedui.data.user.repository

import com.daxen.mydancekmpsharedui.data.user.model.Subscription
import com.daxen.mydancekmpsharedui.data.user.model.UserAcademy
import kotlinx.coroutines.flow.StateFlow

interface AcademyUserRepository {
    val currentAcademy: StateFlow<UserAcademy>
    suspend fun updateCurrentAcademy()
    fun setAcademyInfo(
        name: String,
        nif: String,
        address: String,
        openingTime: String,
        closingTime: String
    )
    fun setLogo(image: ByteArray)
    fun setSubscription(subscription: Subscription)
    suspend fun saveToDatabase()
}