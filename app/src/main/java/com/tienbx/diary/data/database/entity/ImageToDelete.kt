package com.tienbx.diary.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tienbx.diary.util.Constants

@Entity(tableName = Constants.IMAGE_DELETE_TABLE)
class ImageToDelete(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val remoteImagePath: String
)
