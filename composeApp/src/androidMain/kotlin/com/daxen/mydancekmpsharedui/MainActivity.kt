package com.daxen.mydancekmpsharedui

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.daxen.mydancekmpsharedui.features.auth.AuthViewModel
import com.daxen.mydancekmpsharedui.features.auth.PostSplashDestination
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        startKoin {
            androidContext(this@MainActivity)
            modules(
                appModule,
                module {
                    single<Context> { applicationContext } // platformModule inline
                }
            )
        }
        authViewModel.checkUserState()
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                authViewModel.destination.value == null
            }
        }

        setContent {
            val destination by authViewModel.destination.collectAsState()

            destination?.let {
                App(destination = it)
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App(destination = PostSplashDestination.Login)
}