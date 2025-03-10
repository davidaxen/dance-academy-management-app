package com.daxen.mydancekmpsharedui.core.firebase

import com.daxen.mydancekmpsharedui.core.firebase.auth.FirebaseAuthService
import com.daxen.mydancekmpsharedui.core.firebase.auth.FirebaseAuthServiceImpl
import com.daxen.mydancekmpsharedui.core.firebase.user.FirebaseUserService
import com.daxen.mydancekmpsharedui.core.firebase.user.FirebaseUserServiceImpl
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreFirebaseModule = module {
    //Main
    single<FirebaseAuth> { Firebase.auth }
    single<FirebaseFirestore> { Firebase.firestore }

    //Auth
    singleOf(::FirebaseAuthServiceImpl) { bind<FirebaseAuthService>() }

    //User
    singleOf(::FirebaseUserServiceImpl) { bind<FirebaseUserService>() }
}