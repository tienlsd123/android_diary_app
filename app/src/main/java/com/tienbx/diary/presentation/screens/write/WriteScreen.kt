package com.tienbx.diary.presentation.screens.write

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.tienbx.diary.model.Diary
import java.time.ZonedDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WriteScreen(
    uiState: WriteUiState,
    pagerState: PagerState,
    onBackPressed: () -> Unit,
    onDeleteConfirmed: () -> Unit,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    moodName: () -> String,
    onSaveClicked: (Diary) -> Unit,
    onDateTimeUpdated: (ZonedDateTime) -> Unit
) {

    LaunchedEffect(key1 = uiState.mood) {
        pagerState.scrollToPage(uiState.mood.ordinal)
    }

    Scaffold(
        topBar = {
            WriteTopBar(
                selectedDiary = uiState.selectedDiary,
                onBackPressed = onBackPressed,
                onDeleteConfirmed = onDeleteConfirmed,
                moodName = moodName,
                onDateTimeUpdated = onDateTimeUpdated
            )
        },
        content = {
            WriteContent(
                uiState = uiState,
                paddingValues = it,
                title = uiState.title,
                description = uiState.description,
                onTitleChanged = onTitleChanged,
                onDescriptionChanged = onDescriptionChanged,
                pagerState = pagerState,
                onSaveClicked = onSaveClicked
            )

        }
    )
}
