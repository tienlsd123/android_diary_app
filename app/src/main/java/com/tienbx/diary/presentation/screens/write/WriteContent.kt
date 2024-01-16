package com.tienbx.diary.presentation.screens.write

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tienbx.diary.model.Diary
import com.tienbx.diary.model.GalleryImage
import com.tienbx.diary.model.GalleryState
import com.tienbx.diary.model.Mood
import com.tienbx.diary.presentation.component.GalleryUploader
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WriteContent(
    uiState: WriteUiState,
    isAuthor: Boolean = false,
    galleryState: GalleryState,
    paddingValues: PaddingValues,
    title: String,
    description: String,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    pagerState: PagerState,
    onSaveClicked: (Diary) -> Unit,
    onImageSelect: (Uri) -> Unit,
    onImageClicked: (GalleryImage) -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = scrollState.maxValue) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    Column(
        modifier = Modifier
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = 24.dp,
                start = 24.dp,
                end = 24.dp,
            )
            .navigationBarsPadding()
            .fillMaxSize()
            .imePadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState),
            content = {
                Spacer(modifier = Modifier.height(30.dp))
                HorizontalPager(
                    state = pagerState,
                    userScrollEnabled = isAuthor
                ) { page ->
                    val mood = Mood.values()[page]
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            modifier = Modifier.size(120.dp),
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(mood.icon)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Mood Image"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = title,
                    enabled = isAuthor,
                    onValueChange = onTitleChanged,
                    shape = Shapes().small,
                    placeholder = { Text(text = "Title") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Unspecified,
                        disabledIndicatorColor = Color.Unspecified,
                        unfocusedIndicatorColor = Color.Unspecified,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            scope.launch {
                                scrollState.animateScrollTo(Int.MAX_VALUE)
                            }
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    maxLines = 1,
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(5.dp))

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = description,
                    onValueChange = onDescriptionChanged,
                    shape = Shapes().small,
                    enabled = isAuthor,
                    placeholder = { Text(text = "Tell me about it.") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Unspecified,
                        disabledIndicatorColor = Color.Unspecified,
                        unfocusedIndicatorColor = Color.Unspecified,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.clearFocus() }
                    )
                )


            }
        )

        Column(
            verticalArrangement = Arrangement.Bottom,
            content = {
                Spacer(modifier = Modifier.height(12.dp))
                GalleryUploader(
                    isAuthor = isAuthor,
                    galleryState = galleryState,
                    onAddClicked = { focusManager.clearFocus() },
                    onImageSelect = onImageSelect,
                    onImageClicked = onImageClicked
                )
                Spacer(modifier = Modifier.height(12.dp))
                if (!isAuthor) return@Column
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = Shapes().small,
                    onClick = {
                        if (uiState.title.isNotEmpty() && uiState.description.isNotEmpty()) {
                            onSaveClicked(
                                Diary().apply {
                                    this.title = uiState.title
                                    this.description = uiState.description
                                    this.images = galleryState.images.map { it.remoteImagePath }.toRealmList()
                                }
                            )
                        } else {
                            Toast.makeText(context, "Fields cannot be empty.", Toast.LENGTH_SHORT).show()
                        }
                    },
                ) {
                    Text(text = "Save")
                }
            }
        )
    }
}
