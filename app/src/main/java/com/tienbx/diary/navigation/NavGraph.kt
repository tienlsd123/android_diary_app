package com.tienbx.diary.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import com.tienbx.diary.model.Mood
import com.tienbx.diary.presentation.component.DisplayAlertDialog
import com.tienbx.diary.presentation.screens.auth.AuthenticationScreen
import com.tienbx.diary.presentation.screens.auth.AuthenticationViewModel
import com.tienbx.diary.presentation.screens.home.HomeScreen
import com.tienbx.diary.presentation.screens.home.HomeViewModel
import com.tienbx.diary.presentation.screens.write.WriteScreen
import com.tienbx.diary.presentation.screens.write.WriteViewModel
import com.tienbx.diary.util.Constants
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SetupNavGraph(startDestination: String, navHostController: NavHostController) {
    NavHost(startDestination = startDestination, navController = navHostController) {
        authenticationRoute(navigateToHome = {
            navHostController.popBackStack()
            navHostController.navigate(Screen.Home.route)
        })
        homeRoute(
            navigateToWrite = {
                navHostController.navigate(Screen.Write.route)
            },
            navigateToAuth = {
                navHostController.popBackStack()
                navHostController.navigate(Screen.Authentication.route)
            },
            navigateWriteWithArgs = {
                navHostController.navigate(Screen.Write.passDiaryId(diaryId = it))
            }
        )
        writeRoute(onBackPressed = {
            navHostController.popBackStack()
        })
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
                    onSuccess = {
                        messageBarState.addSuccess("Successfully Authenticated")
                    },
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

fun NavGraphBuilder.homeRoute(
    navigateToWrite: () -> Unit,
    navigateWriteWithArgs: (String) -> Unit,
    navigateToAuth: () -> Unit
) {
    composable(route = Screen.Home.route) {
        val viewModel: HomeViewModel = viewModel()
        val diaries by viewModel.diaries
        var signOutDialogOpened by remember { mutableStateOf(false) }
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        HomeScreen(
            diaries = diaries,
            drawerState = drawerState,
            onMenuClicked = {
                scope.launch {
                    drawerState.open()
                }
            },
            onSignOutClicked = { signOutDialogOpened = true },
            navigateToWrite = navigateToWrite,
            navigateWriteWithArgs = navigateWriteWithArgs
        )

        DisplayAlertDialog(
            title = "Sign out",
            msg = "Are you sure you want to Sign Out from your Google Account?",
            dialogOpened = signOutDialogOpened,
            onCloseDialog = { signOutDialogOpened = false },
            onYesClicked = {
                scope.launch(Dispatchers.IO) {
                    val user = App.create(Constants.APP_ID).currentUser
                    if (user != null) {
                        user.logOut()
                        withContext(Dispatchers.Main) {
                            navigateToAuth()
                        }
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun NavGraphBuilder.writeRoute(
    onBackPressed: () -> Unit
) {
    composable(route = Screen.Write.route, arguments = listOf(navArgument(name = Constants.WRITE_SCREEN_ARG_KEY) {
        type = NavType.StringType
        nullable = true
        defaultValue = null
    })) {
        val viewModel: WriteViewModel = viewModel()
        val uiState = viewModel.uiState
        val pagerState = rememberPagerState { Mood.values().size }
        val pageNumber by remember {
            derivedStateOf { pagerState.currentPage }
        }
        val context = LocalContext.current
        WriteScreen(
            uiState = uiState,
            moodName = { Mood.values()[pageNumber].name },
            onTitleChanged = { viewModel.setTitle(it) },
            onDescriptionChanged = { viewModel.setDescription(it) },
            pagerState = pagerState,
            onDeleteConfirmed = {
                viewModel.deleteDiary(
                    onSuccess = {
                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    },
                    onError = { msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                )
            },
            onBackPressed = onBackPressed,
            onSaveClicked = {
                viewModel.upsertDiary(
                    diary = it.apply { mood = Mood.values()[pageNumber].name },
                    onSuccess = { onBackPressed() },
                    onError = { msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    },
                )
            },
            onDateTimeUpdated = { viewModel.updateDateTime(it) }
        )
    }
}
