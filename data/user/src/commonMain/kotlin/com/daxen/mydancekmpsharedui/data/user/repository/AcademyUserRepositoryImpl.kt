package com.daxen.mydancekmpsharedui.data.user.repository

import com.daxen.mydancekmpsharedui.core.firebase.auth.FirebaseAuthService
import com.daxen.mydancekmpsharedui.core.firebase.user.FirebaseAcademyUserService
import com.daxen.mydancekmpsharedui.data.user.model.Subscription
import com.daxen.mydancekmpsharedui.data.user.model.UserAcademy
import com.daxen.mydancekmpsharedui.data.user.model.mapper.toAcademyModel
import com.daxen.mydancekmpsharedui.data.user.model.mapper.toAcademyUserModel
import com.daxen.mydancekmpsharedui.data.user.model.mapper.toUserAcademy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AcademyUserRepositoryImpl(
    private val firebaseAuthService: FirebaseAuthService,
    private val firebaseAcademyUserService: FirebaseAcademyUserService
): AcademyUserRepository {
    private val _currentAcademy = MutableStateFlow(UserAcademy.EMPTY)
    override val currentAcademy: StateFlow<UserAcademy> = _currentAcademy

    private val _selectedLogo = MutableStateFlow(ByteArray(0))

    override suspend fun updateCurrentAcademy() {
        val academyUserResponse = firebaseAcademyUserService.getCurrentAcademyUserData()
        val academyResponse = firebaseAcademyUserService.getCurrentAcademyData(academyUserResponse.academyId)

        _currentAcademy.value = toUserAcademy(
            academyUserModel = academyUserResponse,
            academyModel = academyResponse
        )
    }

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
    }

    override fun setLogo(image: ByteArray) {
        _selectedLogo.value = image
    }

    override fun setSubscription(subscription: Subscription) {
        _currentAcademy.value.subscription = subscription
    }

    override suspend fun saveToDatabase() {
        _currentAcademy.value.email = firebaseAuthService.getCurrentUserEmail()
            ?: throw IllegalStateException("Email de usuario nulo")
        _currentAcademy.value.uid = firebaseAuthService.getCurrentUserId()
            ?: throw IllegalStateException("ID de usuario nulo")

        firebaseAcademyUserService.saveUserToDatabase(
            academyUserModel = _currentAcademy.value.toAcademyUserModel(),
            academyModel = _currentAcademy.value.toAcademyModel(),
            image = _selectedLogo.value
        )
    }
}