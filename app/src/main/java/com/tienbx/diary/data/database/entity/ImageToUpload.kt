package com.tienbx.diary.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tienbx.diary.util.Constants

@Entity(tableName = Constants.IMAGE_UPLOAD_TABLE)
data class ImageToUpload(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val remoteImagePath: String = "",
    val imageUri: String = "",
    var sessionUri: String = "",
)
