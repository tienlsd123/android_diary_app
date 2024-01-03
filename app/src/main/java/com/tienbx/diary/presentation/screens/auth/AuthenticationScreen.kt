package com.tienbx.diary.presentation.screens.auth

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticationScreen(
    loadingState: Boolean,
    onButtonClick: () -> Unit
) {
    Scaffold(content = {
        AuthenticationContent(loadingState = loadingState, onButtonClick = onButtonClick)
    })
}
