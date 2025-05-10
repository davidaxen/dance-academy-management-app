package com.daxen.mydancekmpsharedui.data.user.model.mapper

import com.daxen.mydancekmpsharedui.core.firebase.user.models.AcademyModel
import com.daxen.mydancekmpsharedui.core.firebase.user.models.AcademyUserModel
import com.daxen.mydancekmpsharedui.core.firebase.user.models.SubscriptionModel
import com.daxen.mydancekmpsharedui.data.user.model.Subscription
import com.daxen.mydancekmpsharedui.data.user.model.UserAcademy

fun UserAcademy.toAcademyUserModel(): AcademyUserModel {
    return AcademyUserModel(
        uid = this.uid,
        email = this.email,
        name = this.name,
        role = this.role.roleName
    )
}

fun UserAcademy.toAcademyModel(): AcademyModel {
    return AcademyModel(
        name = this.name,
        nif = this.nif,
        address = this.address,
        openingTime = this.openingTime,
        closingTime = this.closingTime,
        subscription = this.subscription?.toSubscriptionModel()
    )
}

fun Subscription.toSubscriptionModel(): SubscriptionModel {
    return SubscriptionModel(
        plan = this.plan.planName,
        addons = this.addons.map { it.addOnName }
    )
}