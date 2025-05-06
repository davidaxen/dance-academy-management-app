package com.daxen.mydancekmpsharedui.data.user.repository

import com.daxen.mydancekmpsharedui.data.user.model.Subscription
import com.daxen.mydancekmpsharedui.data.user.model.UserAcademy
import kotlinx.coroutines.flow.MutableStateFlow

class AcademyUserRepositoryImpl: AcademyUserRepository {
    private val _currentAcademy = MutableStateFlow(UserAcademy.EMPTY)
    private val _selectedLogo = MutableStateFlow<ByteArray?>(null)

    override fun setAcademyInfo(
        name: String,
        nif: String,
        address: String,
        openingTime: String,
        closingTime: String
    ) {
        _currentAcademy.value.name = name
        _currentAcademy.value.nif = nif
        _currentAcademy.value.address = address
        _currentAcademy.value.openingTime = openingTime
        _currentAcademy.value.closingTime = closingTime

        println(_currentAcademy.value)
    }

    override fun setLogo(image: ByteArray) {
        _selectedLogo.value = image
        println(_selectedLogo.value)
    }

    override fun setSubscription(subscription: Subscription) {
        _currentAcademy.value.subscription = subscription
        println(_currentAcademy.value)
    }

    override suspend fun saveToDatabase() {

    }
}