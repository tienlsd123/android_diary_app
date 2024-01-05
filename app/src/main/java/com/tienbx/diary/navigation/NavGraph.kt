package com.tienbx.diary.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import com.tienbx.diary.presentation.screens.auth.AuthenticationScreen
import com.tienbx.diary.presentation.screens.auth.AuthenticationViewModel
import com.tienbx.diary.presentation.screens.home.HomeScreen
import com.tienbx.diary.util.Constants
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

@Composable
fun SetupNavGraph(startDestination: String, navHostController: NavHostController) {
    NavHost(startDestination = startDestination, navController = navHostController) {
        authenticationRoute(navigateToHome = {
            navHostController.popBackStack()
            navHostController.navigate(Screen.Home.route)
        })
        homeRoute(navigateToWrite = {
            navHostController.navigate(Screen.Write.route)
        })
        writeRoute()
    }
}

fun NavGraphBuilder.authenticationRoute(navigateToHome: () -> Unit) {
    composable(route = Screen.Authentication.route) {
        val authVieModel: AuthenticationViewModel = viewModel()
        val loadingState by authVieModel.loadingState
        val authenticated = authVieModel.authenticated.value

        val oneTapSignInState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()


        AuthenticationScreen(
            loadingState = loadingState,
            oneTapSignInState = oneTapSignInState,
            messageBarState = messageBarState,
            authenticated = authenticated,
            onButtonClick = {
                oneTapSignInState.open()
                authVieModel.setLoading(true)
            },
            onTokenIdReceived = { tokenId ->
                authVieModel.signWithMongoAtlas(
                    tokenId = tokenId,
                    onSuccess = { messageBarState.addSuccess("Successfully Authenticated") },
                    onError = { messageBarState.addError(it) }
                )
            },
            onDialogDismiss = { msg ->
                messageBarState.addError(Exception(msg))
                authVieModel.setLoading(false)
            },
            navigateHomeScreen = navigateToHome
        )
    }
}

fun NavGraphBuilder.homeRoute(navigateToWrite: () -> Unit) {
    composable(route = Screen.Home.route) {
        HomeScreen(
            onMenuClicked = {

            },
            navigateToWrite = navigateToWrite
        )
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
