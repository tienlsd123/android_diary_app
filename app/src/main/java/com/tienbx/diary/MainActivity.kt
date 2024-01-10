package com.tienbx.diary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.coroutineScope
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.tienbx.diary.data.database.ImagesToDeleteDao
import com.tienbx.diary.data.database.ImagesToUploadDao
import com.tienbx.diary.navigation.Screen
import com.tienbx.diary.navigation.SetupNavGraph
import com.tienbx.diary.ui.theme.DiaryAppTheme
import com.tienbx.diary.util.Constants
import com.tienbx.diary.util.retryDeletingImageFromFirebase
import com.tienbx.diary.util.retryUploadingImageToFirebase
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imagesToUploadDao: ImagesToUploadDao

    @Inject
    lateinit var imagesToDeleteDao: ImagesToDeleteDao

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            DiaryAppTheme(dynamicColor = false) {
                val navController = rememberNavController()
                SetupNavGraph(startDestination = getStartDestination(), navHostController = navController)
            }
        }
        cleanupCheck(
            scope = lifecycle.coroutineScope,
            imagesToUploadDao = imagesToUploadDao,
            imagesToDeleteDao = imagesToDeleteDao
        )
    }
}

private fun cleanupCheck(
    scope: CoroutineScope,
    imagesToUploadDao: ImagesToUploadDao,
    imagesToDeleteDao: ImagesToDeleteDao
) {
    scope.launch(Dispatchers.IO) {
        val uploadFailed = imagesToUploadDao.getAllImages()
        uploadFailed.forEach { imageToUpload ->
            retryUploadingImageToFirebase(
                imageToUpload = imageToUpload,
                onSuccess = {
                    scope.launch(Dispatchers.IO) {
                        imagesToUploadDao.cleanupImage(imageToUpload)
                    }
                }
            )
        }
        val deleteFailed = imagesToDeleteDao.getAllImages()
        deleteFailed.forEach { image ->
            retryDeletingImageFromFirebase(
                imageToDelete = image,
                onSuccess = {

                }
            )
        }

    }
}


private fun getStartDestination(): String {
    val user = App.create(Constants.APP_ID).currentUser
    return if (user != null && user.loggedIn) Screen.Home.route else Screen.Authentication.route
}
