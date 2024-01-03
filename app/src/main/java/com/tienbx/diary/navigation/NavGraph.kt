package com.tienbx.diary.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tienbx.diary.presentation.screens.auth.AuthenticationScreen
import com.tienbx.diary.util.Constants

@Composable
fun SetupNavGraph(startDestination: String, navHostController: NavHostController) {
    NavHost(startDestination = startDestination, navController = navHostController) {
        authenticationRoute()
        homeRoute()
        writeRoute()
    }
}

fun NavGraphBuilder.authenticationRoute() {
    composable(route = Screen.Authentication.route) {
        AuthenticationScreen(loadingState = false, onButtonClick = {})
    }
}

fun NavGraphBuilder.homeRoute() {
    composable(route = Screen.Home.route) {

    }
}

fun NavGraphBuilder.writeRoute() {
    composable(route = Screen.Write.route, arguments = listOf(navArgument(name = Constants.WRITE_SCREEN_ARG_KEY) {
        type = NavType.StringType
        nullable = true
        defaultValue = null
    })) {

    }
}
