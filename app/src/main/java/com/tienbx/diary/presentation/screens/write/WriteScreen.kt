package com.tienbx.diary.presentation.screens.write

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tienbx.diary.model.Diary
import com.tienbx.diary.model.GalleryImage
import com.tienbx.diary.model.GalleryState
import java.time.ZonedDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WriteScreen(
    uiState: WriteUiState,
    isAuthor: Boolean,
    pagerState: PagerState,
    galleryState: GalleryState,
    onBackPressed: () -> Unit,
    onDeleteConfirmed: () -> Unit,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    moodName: () -> String,
    onSaveClicked: (Diary) -> Unit,
    onDateTimeUpdated: (ZonedDateTime) -> Unit,
    onImageSelect: (Uri) -> Unit,
    onImageDeleteClicked: (GalleryImage) -> Unit
) {

    var selectedGalleryImage by remember {
        mutableStateOf<GalleryImage?>(null)
    }

    LaunchedEffect(key1 = uiState.mood) {
        pagerState.scrollToPage(uiState.mood.ordinal)
    }

    Scaffold(
        topBar = {
            WriteTopBar(
                isAuthor = isAuthor,
                selectedDiary = uiState.selectedDiary,
                onBackPressed = onBackPressed,
                onDeleteConfirmed = onDeleteConfirmed,
                moodName = moodName,
                onDateTimeUpdated = onDateTimeUpdated
            )
        },
        content = { padding ->
            WriteContent(
                isAuthor = isAuthor,
                uiState = uiState,
                galleryState = galleryState,
                paddingValues = padding,
                title = uiState.title,
                description = uiState.description,
                onTitleChanged = onTitleChanged,
                onDescriptionChanged = onDescriptionChanged,
                pagerState = pagerState,
                onSaveClicked = onSaveClicked,
                onImageSelect = onImageSelect,
                onImageClicked = { galleryImage ->
                    selectedGalleryImage = galleryImage
                }
            )

            AnimatedVisibility(visible = selectedGalleryImage != null) {
                Dialog(onDismissRequest = { selectedGalleryImage = null }) {
                    selectedGalleryImage?.let {
                        ZoomableImage(
                            isAuthor = isAuthor,
                            selectedGalleryImage = selectedGalleryImage!!,
                            onCloseClicked = {
                                selectedGalleryImage = null
                            },
                            onDeleteClicked = {
                                selectedGalleryImage?.let {
                                    onImageDeleteClicked(it)
                                    selectedGalleryImage = null
                                }
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun ZoomableImage(
    isAuthor: Boolean,
    selectedGalleryImage: GalleryImage,
    onCloseClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var scale by remember { mutableFloatStateOf(1f) }
    Box(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = maxOf(1f, minOf(scale * zoom, 5f))
                    val maxX = (size.width * (scale - 1)) / 2
                    val minX = -maxX
                    offsetX = maxOf(minX, minOf(maxX, offsetX + pan.x))
                    val maxY = (size.height * (scale - 1)) / 2
                    val minY = -maxY
                    offsetY = maxOf(minY, minOf(maxY, offsetY + pan.y))
                }
            }
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = maxOf(.5f, minOf(3f, scale)),
                    scaleY = maxOf(.5f, minOf(3f, scale)),
                    translationX = offsetX,
                    translationY = offsetY
                ),
            model = ImageRequest.Builder(LocalContext.current)
                .data(selectedGalleryImage.image.toString())
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Fit,
            contentDescription = "Gallery Image"
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = onCloseClicked) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close Icon")
                Text(text = "Close")
            }
            if (isAuthor) {
                Button(onClick = onDeleteClicked) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Icon")
                    Text(text = "Delete")
                }
            }
        }
    }
}

