package com.daxen.mydancekmpsharedui.data.user.model.mapper

import com.daxen.mydancekmpsharedui.core.firebase.user.models.AcademyModel
import com.daxen.mydancekmpsharedui.core.firebase.user.models.AcademyUserModel
import com.daxen.mydancekmpsharedui.core.firebase.user.models.SubscriptionModel
import com.daxen.mydancekmpsharedui.data.user.model.AddOnType
import com.daxen.mydancekmpsharedui.data.user.model.PlanType
import com.daxen.mydancekmpsharedui.data.user.model.Subscription
import com.daxen.mydancekmpsharedui.data.user.model.UserAcademy
import com.daxen.mydancekmpsharedui.data.user.model.UserRole

fun UserAcademy.toAcademyUserModel(): AcademyUserModel {
    return AcademyUserModel(
        uid = this.uid,
        academyId = this.academyId,
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

fun SubscriptionModel.toSubscription(): Subscription? {
    return if (this.addons.isEmpty()) {
        null
    } else {
        Subscription(
            plan = PlanType.from(this.plan) ?: PlanType.STARTER,
            addons = this.addons.mapNotNull { AddOnType.from(it) }
        )
    }
}

fun toUserAcademy(academyUserModel: AcademyUserModel, academyModel: AcademyModel): UserAcademy {
    return UserAcademy(
        uid = academyUserModel.uid,
        academyId = academyUserModel.academyId,
        email = academyUserModel.email,
        name = academyUserModel.name,
        nif = academyModel.nif,
        address = academyModel.address,
        openingTime = academyModel.openingTime,
        closingTime = academyModel.closingTime,
        subscription = academyModel.subscription?.toSubscription(),
        role = UserRole.ACADEMY
    )
}
