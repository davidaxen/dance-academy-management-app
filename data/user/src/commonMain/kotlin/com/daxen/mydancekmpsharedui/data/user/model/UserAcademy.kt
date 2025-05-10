package com.daxen.mydancekmpsharedui.data.user.model

data class UserAcademy(
    var uid: String = "",
    var email: String = "",
    var name: String = "",
    var nif: String = "",
    var address: String = "",
    var openingTime: String = "",
    var closingTime: String = "",
    var subscription: Subscription? = null,
    var role: UserRole = UserRole.ACADEMY
) {
    companion object {
        val EMPTY = UserAcademy()
    }
}