package com.daxen.mydancekmpsharedui.data.auth.model

data class User(
    var uid: String,
    var email: String,
    var role: UserRole,
    var name: String,
)