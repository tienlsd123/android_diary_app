package com.tienbx.diary.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tienbx.diary.data.database.entity.ImageToUpload
import com.tienbx.diary.util.Constants

@Dao
interface ImagesToUploadDao {

    @Query("SELECT * FROM ${Constants.IMAGE_UPLOAD_TABLE} ORDER BY id ASC")
    suspend fun getAllImages(): List<ImageToUpload>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImageToUpLoad(imageToUpload: ImageToUpload)

    @Delete
    suspend fun cleanupImage(imageToUpload: ImageToUpload)
}
