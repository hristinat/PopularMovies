package com.example.android.popularmovies.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteMovie::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteMovieDao(): FavoriteMovieDao?

    companion object {
        private val LOCK = Any()
        private const val DATABASE_NAME = "movie"
        private var sInstance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase? {
            if (sInstance == null) {
                synchronized(LOCK) {
                    sInstance = Room.databaseBuilder(context.applicationContext,
                            AppDatabase::class.java, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }
            return sInstance
        }
    }
}