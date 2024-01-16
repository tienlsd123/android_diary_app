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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.tienbx.diary.util.Constants
import com.tienbx.diary.util.OneTapSignInState
import com.tienbx.diary.util.OneTapSignInWithGoogle

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticationScreen(
    loadingState: Boolean,
    oneTapSignInState: OneTapSignInState,
    messageBarState: MessageBarState,
    authenticated: Boolean,
    onButtonClick: () -> Unit,
    onSuccessFireBaseSignIn: (tokenId: String) -> Unit,
    onFailedFirebaseSignIn: (Exception) -> Unit,
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

    OneTapSignInWithGoogle(
        state = oneTapSignInState,
        clientId = Constants.CLIENT_ID,
        onTokenIdReceived = { tokenId ->
            val credential = GoogleAuthProvider.getCredential(tokenId, null)
            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccessFireBaseSignIn(tokenId)
                } else {
                    it.exception?.let { exception -> onFailedFirebaseSignIn(exception) }
                }
            }
        },
        onDialogDismissed = { msg ->
            onDialogDismiss(msg)
        }
    )

    LaunchedEffect(key1 = authenticated) {
        if (authenticated) {
            navigateHomeScreen()
        }
    }
}
