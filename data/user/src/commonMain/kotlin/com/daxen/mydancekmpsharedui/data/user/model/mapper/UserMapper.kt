package com.daxen.mydancekmpsharedui.data.user.model.mapper

import com.daxen.mydancekmpsharedui.core.firebase.user.models.UserAcademyInfoResponse
import com.daxen.mydancekmpsharedui.core.firebase.user.models.UserModel
import com.daxen.mydancekmpsharedui.data.user.model.DanceRole
import com.daxen.mydancekmpsharedui.data.user.model.User
import com.daxen.mydancekmpsharedui.data.user.model.UserAcademyInfo
import com.daxen.mydancekmpsharedui.data.user.model.UserRole

fun UserModel.toUser(): User {
    val role = UserRole.from(this.role)
        ?: UserRole.STUDENT

    val danceRole = DanceRole.from(this.danceRole)
        ?: DanceRole.LEADER

//    val academiesResponse: Map<String, UserAcademyInfo> =
//        this.academies.map { (academyId, academyInfoResponse) ->
//            val roleResponse = UserRole.from(academyInfoResponse.role) ?: UserRole.STUDENT
//            academyId to UserAcademyInfo(role = roleResponse)
//        }.toMap()

    return User(
        uid = this.uid,
        email = this.email,
        name = this.name,
        lastName  = this.lastName,
        birthDate = this.birthDate,
        phoneNumber = this.phoneNumber,
        phoneNumberPrefix = this.phoneNumberPrefix,
        role = role,
        danceRole = danceRole,
//        academies = academiesResponse
    )
}

fun User.toUserResponse(): UserModel {
//    val academiesResponse: Map<String, UserAcademyInfoResponse> =
//        this.academies.map { (academyId, academyInfo) ->
//            academyId to UserAcademyInfoResponse(
//                role = academyInfo.role.roleName
//            )
//        }.toMap()

    return UserModel(
        uid = this.uid,
        email = this.email,
        name = this.name,
        lastName = this.lastName,
        birthDate = this.birthDate,
        phoneNumber = this.phoneNumber,
        phoneNumberPrefix = this.phoneNumberPrefix,
        role = this.role.roleName,
        danceRole = this.danceRole.roleName,
//        academies = academiesResponse
    )
}