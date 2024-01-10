package com.tienbx.diary.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.tienbx.diary.data.database.entity.ImageToDelete

@Dao
interface ImagesToDeleteDao {
    @Query("SELECT * FROM image_to_delete_table ORDER BY id ASC")
    suspend fun getAllImages() : List<ImageToDelete>

    @Insert
    suspend fun addImageToDelete(imageToDelete: ImageToDelete)

    @Delete
    suspend fun cleanupImage(imageToDelete: ImageToDelete)
}
