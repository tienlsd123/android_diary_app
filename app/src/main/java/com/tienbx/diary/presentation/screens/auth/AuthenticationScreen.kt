package com.tienbx.diary.presentation.screens.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle
import com.tienbx.diary.util.Constants

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticationScreen(
    loadingState: Boolean,
    oneTapSignInState: OneTapSignInState,
    messageBarState: MessageBarState,
    authenticated: Boolean,
    onButtonClick: () -> Unit,
    onTokenIdReceived: (tokenId: String) -> Unit,
    onDialogDismiss: (msg: String) -> Unit,
    navigateHomeScreen: () -> Unit,
) {
    Scaffold(modifier = Modifier
        .background(MaterialTheme.colorScheme.surface)
        .statusBarsPadding()
        .navigationBarsPadding(),
        content = {
            ContentWithMessageBar(messageBarState = messageBarState) {
                AuthenticationContent(loadingState = loadingState, onButtonClick = onButtonClick)
            }
        })

    OneTapSignInWithGoogle(state = oneTapSignInState, clientId = Constants.CLIENT_ID, onTokenIdReceived = { tokenId ->
        onTokenIdReceived(tokenId)
    }, onDialogDismissed = { msg ->
        onDialogDismiss(msg)
    })

    LaunchedEffect(key1 = authenticated) {
        if (authenticated) {
            navigateHomeScreen()
        }
    }
}
