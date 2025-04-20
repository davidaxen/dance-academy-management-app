package com.daxen.mydancekmpsharedui.data.user.model

enum class DanceRole(val roleName: String) {
    LEADER("leader"),
    FOLLOWER("follower");

    companion object {
        fun from(roleName: String?): DanceRole? {
            return DanceRole.entries.find { it.roleName == roleName }
        }
    }
}