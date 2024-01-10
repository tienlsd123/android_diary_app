package com.tienbx.diary.presentation.screens.write

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.storage.FirebaseStorage
import com.tienbx.diary.data.database.ImagesToDeleteDao
import com.tienbx.diary.data.database.ImagesToUploadDao
import com.tienbx.diary.data.database.entity.ImageToDelete
import com.tienbx.diary.data.database.entity.ImageToUpload
import com.tienbx.diary.data.repository.MongoDB
import com.tienbx.diary.model.Diary
import com.tienbx.diary.model.GalleryImage
import com.tienbx.diary.model.GalleryState
import com.tienbx.diary.model.Mood
import com.tienbx.diary.util.Constants.WRITE_SCREEN_ARG_KEY
import com.tienbx.diary.util.RequestState
import com.tienbx.diary.util.fetchImagesFromFirebase
import com.tienbx.diary.util.toRealmInstant
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.BsonObjectId
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val imagesToUploadDao: ImagesToUploadDao,
    private val imagesToDeleteDao: ImagesToDeleteDao,
) : ViewModel() {

    val galleryState = GalleryState()

    var uiState by mutableStateOf(WriteUiState())
        private set

    init {
        getDiaryIdArgument()
        fetchSelectedDiary()
    }

    private fun getDiaryIdArgument() {
        uiState = uiState.copy(
            selectedId = savedStateHandle.get<String>(
                key = WRITE_SCREEN_ARG_KEY
            )
        )
    }

    private fun fetchSelectedDiary() {
        if (uiState.selectedId != null) {
            viewModelScope.launch(Dispatchers.Main) {
                MongoDB.getSelectedDiary(diaryId = BsonObjectId(uiState.selectedId!!)).catch {
                    emit(RequestState.Error(Exception("Diary is already deleted.")))
                }.collect { response ->
                    if (response is RequestState.Success) {
                        withContext(Dispatchers.Main) {
                            val data = response.data
                            setSelectedDiary(data)
                            setDescription(data.description)
                            setTitle(data.title)
                            setMood(Mood.valueOf(data.mood))

                            fetchImagesFromFirebase(
                                remoteImagePaths = data.images,
                                onImageDownload = { downloadedImage ->
                                    galleryState.addImage(
                                        GalleryImage(
                                            image = downloadedImage,
                                            remoteImagePath = extractImagePath(
                                                fullImageUrl = downloadedImage.toString()
                                            ),
                                        )
                                    )
                                }
                            )
                        }
                    }
                }

            }
        }
    }

    fun setMood(value: Mood) {
        uiState = uiState.copy(mood = value)
    }

    fun setDescription(value: String) {
        uiState = uiState.copy(description = value)
    }

    fun setTitle(value: String) {
        uiState = uiState.copy(title = value)
    }

    fun setSelectedDiary(diary: Diary) {
        uiState = uiState.copy(selectedDiary = diary)
    }

    fun updateDateTime(zonedDateTime: ZonedDateTime) {
        uiState = uiState.copy(
            updatedDateTime = zonedDateTime.toInstant()?.toRealmInstant()
        )
    }

    private suspend fun insertDiary(
        diary: Diary,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        when (val result = MongoDB.insertNewDiary(diary.apply {
            if (uiState.updatedDateTime != null) {
                date = uiState.updatedDateTime!!
            }
        })) {
            is RequestState.Error -> withContext(Dispatchers.Main) { onError(result.error.message.toString()) }
            is RequestState.Success -> {
                uploadImagesToFirebase()
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            }

            else -> {}
        }
    }

    fun upsertDiary(
        diary: Diary,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (uiState.selectedId != null) {
                updateDiary(
                    onError = onError,
                    onSuccess = onSuccess,
                    diary = diary
                )
            } else {
                insertDiary(
                    onError = onError,
                    onSuccess = onSuccess,
                    diary = diary
                )
            }
        }
    }

    private suspend fun updateDiary(
        diary: Diary,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        when (val result = MongoDB.updateDiary(diary.apply {
            _id = BsonObjectId(uiState.selectedId!!)
            date = if (uiState.updatedDateTime != null)
                uiState.updatedDateTime!!
            else
                uiState.selectedDiary!!.date
        })) {
            is RequestState.Error -> withContext(Dispatchers.Main) { onError(result.error.message.toString()) }
            is RequestState.Success -> {
                uploadImagesToFirebase()
                deleteImagesFromFireBase()
                withContext(Dispatchers.Main) { onSuccess() }
            }

            else -> {}
        }
    }

    fun deleteDiary(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (uiState.selectedId != null) {
                when (val result = MongoDB.deleteDiary(diaryId = BsonObjectId(uiState.selectedId!!))) {
                    is RequestState.Error -> withContext(Dispatchers.Main) { onError(result.error.message.toString()) }
                    is RequestState.Success -> withContext(Dispatchers.Main) {
                        uiState.selectedDiary?.let { deleteImagesFromFireBase(images = it.images) }
                        onSuccess()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun deleteImagesFromFireBase(images: List<String>? = null) {
        val storage = FirebaseStorage.getInstance().reference
        val imagePaths = images ?: galleryState.imagesToBeDeleted.map { it.remoteImagePath }
        imagePaths.forEach { remotePath ->
            storage.child(remotePath).delete().addOnFailureListener {
                viewModelScope.launch(Dispatchers.IO) {
                    imagesToDeleteDao.addImageToDelete(ImageToDelete(remoteImagePath = remotePath))
                }
            }
        }
    }

    fun addImage(image: Uri, imageType: String) {
        val remoteImagePath =
            "images/${FirebaseAuth.getInstance().currentUser?.uid}/${image.lastPathSegment}-${System.currentTimeMillis()}.$imageType"
        galleryState.addImage(GalleryImage(image = image, remoteImagePath))
    }

    private fun uploadImagesToFirebase() {
        val storage = FirebaseStorage.getInstance().reference
        galleryState.images.forEach { galleryImage ->
            val path = storage.child(galleryImage.remoteImagePath)
            val imageToUpload = ImageToUpload(
                remoteImagePath = galleryImage.remoteImagePath,
                imageUri = galleryImage.image.toString(),
            )
            var sessionUri: Uri? = null
            path.putFile(galleryImage.image)
                .addOnProgressListener {
                    sessionUri = it.uploadSessionUri
                }.addOnFailureListener {
                    if (sessionUri != null) {
                        viewModelScope.launch(Dispatchers.IO) {
                            imageToUpload.sessionUri = sessionUri.toString()
                            imagesToUploadDao.addImageToUpLoad(imageToUpload)
                        }
                    }
                }
        }
    }

    private fun extractImagePath(fullImageUrl: String): String {
        val chunks = fullImageUrl.split("%2F")
        val imageName = chunks.last().split("?").first()
        return "images/${Firebase.auth.currentUser?.uid}/$imageName"
    }
}

data class WriteUiState(
    val selectedId: String? = null,
    val selectedDiary: Diary? = null,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.Happy,
    val updatedDateTime: RealmInstant? = null
)
