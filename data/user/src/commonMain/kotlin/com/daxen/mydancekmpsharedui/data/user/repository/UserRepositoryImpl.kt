package com.daxen.mydancekmpsharedui.data.user.repository

import com.daxen.mydancekmpsharedui.core.firebase.auth.FirebaseAuthService
import com.daxen.mydancekmpsharedui.core.firebase.user.FirebaseUserService
import com.daxen.mydancekmpsharedui.data.user.model.DanceRole
import com.daxen.mydancekmpsharedui.data.user.model.User
import com.daxen.mydancekmpsharedui.data.user.model.UserRole
import com.daxen.mydancekmpsharedui.data.user.model.mapper.toUser
import com.daxen.mydancekmpsharedui.data.user.model.mapper.toUserResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserRepositoryImpl(
    private val firebaseUserService: FirebaseUserService,
    private val firebaseAuthService: FirebaseAuthService
): UserRepository {
    private val _currentUser = MutableStateFlow(User.EMPTY)
    override val currentUser: StateFlow<User> get() = _currentUser

    init {
        CoroutineScope(Dispatchers.IO).launch {
            updateCurrentUser()
        }
    }

    override suspend fun updateCurrentUser() {
        val userResponse = firebaseUserService.getCurrentUserData()
        _currentUser.value =  userResponse.toUser()
    }

    override suspend fun isUserInfoComplete(): Boolean {
        val value = firebaseUserService.isUserInfoComplete()
        if (value) {
            updateCurrentUser()
        }
        return value
    }

    override fun logOut() {
        _currentUser.value = User.EMPTY
    }

    override fun setPersonalInfo(
        firstName: String,
        lastName: String,
        birthDate: String,
        phone: String,
        prefix: String
    ) {
        _currentUser.value.name = firstName
        _currentUser.value.lastName = lastName
        _currentUser.value.birthDate = birthDate
        _currentUser.value.phoneNumber = phone
        _currentUser.value.phoneNumberPrefix = prefix
    }

    override fun setDanceRole(role: DanceRole) {
        _currentUser.value.danceRole = role
    }

    override fun setRole(role: UserRole) {
        _currentUser.value.role = role
//        _currentUser.value.academies = mapOf(
//            "CJK3TNrlIeIXdKYeI5Ee" to UserAcademyInfo(
//                role = role
//            )
//        )
    }
    override suspend fun saveUserToDatabase() {
        _currentUser.value.email = firebaseAuthService.getCurrentUserEmail()
            ?: throw IllegalStateException("Email de usuario nulo")

        firebaseUserService.saveUserToDatabase(_currentUser.value.toUserResponse())
    }

}