package com.daxen.mydancekmpsharedui.features.user

import com.daxen.mydancekmpsharedui.data.user.repository.AcademyUserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

interface AcademyUserInfoProvider {
    val userAcademyUiFlow: StateFlow<UserAcademyUi>
}

class AcademyUserInfoProviderImpl(
    academyUserRepository: AcademyUserRepository
) : AcademyUserInfoProvider {
    override val userAcademyUiFlow: StateFlow<UserAcademyUi> =
        academyUserRepository.currentAcademy
            .map { userAcademy ->
                userAcademy.let {
                    UserAcademyUi(
                        name = it.name,
                        email = it.email,
                        address = it.address,
                        logoUrl = it.logoUrl
                    )
                }
            }
            .stateIn(
                scope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
                started = SharingStarted.Eagerly,
                initialValue = UserAcademyUi.Loading
            )
}

data class UserAcademyUi(
    val name: String,
    val email: String,
    val address: String,
    val logoUrl: String
) {
    companion object {
        val Loading = UserAcademyUi(
            name = "",
            email = "",
            address = "",
            logoUrl = ""
        )
    }
}