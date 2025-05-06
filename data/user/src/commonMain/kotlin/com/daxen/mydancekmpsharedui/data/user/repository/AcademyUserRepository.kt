package com.daxen.mydancekmpsharedui.data.user.repository

import com.daxen.mydancekmpsharedui.data.user.model.Subscription

interface AcademyUserRepository {
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