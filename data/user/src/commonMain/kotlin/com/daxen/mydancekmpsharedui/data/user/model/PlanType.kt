package com.daxen.mydancekmpsharedui.data.user.model

enum class PlanType(val planName: String) {
    STARTER("starter"),
    PRO("pro"),
    ELITE("elite");

    companion object {
        fun from(planName: String?): PlanType? {
            return PlanType.entries.find { it.planName == planName }
        }
    }
}