package com.tienbx.diary.di

import android.content.Context
import androidx.room.Room
import com.tienbx.diary.data.database.ImagesDatabase
import com.tienbx.diary.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ImagesDatabase = Room.databaseBuilder(
        context = context,
        klass = ImagesDatabase::class.java,
        name = Constants.IMAGES_DATABASE
    ).build()

    @Provides
    @Singleton
    fun provideImageToUploadDao(database: ImagesDatabase) = database.imageToUploadDao()

    @Provides
    @Singleton
    fun provideImageToDeleteDao(database: ImagesDatabase) = database.imageToDeleteDao()
}
