package com.tienbx.diary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.tienbx.diary.navigation.Screen
import com.tienbx.diary.navigation.SetupNavGraph
import com.tienbx.diary.ui.theme.DiaryAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            DiaryAppTheme {
                val navController = rememberNavController()
                SetupNavGraph(startDestination = Screen.Authentication.route, navHostController = navController)
            }
        }
    }
}
