package com.tienbx.diary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.tienbx.diary.data.repository.MongoDB
import com.tienbx.diary.navigation.Screen
import com.tienbx.diary.navigation.SetupNavGraph
import com.tienbx.diary.ui.theme.DiaryAppTheme
import com.tienbx.diary.util.Constants
import io.realm.kotlin.mongodb.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MongoDB.configureTheRealm()
        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            DiaryAppTheme(dynamicColor = false) {
                val navController = rememberNavController()
                SetupNavGraph(startDestination = getStartDestination(), navHostController = navController)
            }
        }
    }
}

private fun getStartDestination(): String {
    val user = App.create(Constants.APP_ID).currentUser
    return if (user != null && user.loggedIn) Screen.Home.route else Screen.Authentication.route
}
