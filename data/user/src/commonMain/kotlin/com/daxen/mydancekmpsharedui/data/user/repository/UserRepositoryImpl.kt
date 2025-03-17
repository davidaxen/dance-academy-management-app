package com.daxen.mydancekmpsharedui.data.user.repository

import com.daxen.mydancekmpsharedui.core.firebase.user.FirebaseUserService
import com.daxen.mydancekmpsharedui.data.user.model.User
import com.daxen.mydancekmpsharedui.data.user.model.mapper.toUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserRepositoryImpl(
    private val firebaseUserService: FirebaseUserService,
): UserRepository {
    private val _currentUser = MutableStateFlow(User.EMPTY)
    override val currentUser: StateFlow<User> get() = _currentUser

    init {
        CoroutineScope(Dispatchers.IO).launch {
            updateCurrentUser()
        }
    }

    override suspend fun updateCurrentUser() {
        val userResponse = firebaseUserService.getCurrentUserData()?.toUser() ?: User.EMPTY
        _currentUser.value =  userResponse
    }

}