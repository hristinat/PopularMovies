package com.example.android.popularmovies.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteMovieDao {
    @Query("SELECT * FROM favorite_movie")
    fun loadAllFavoriteMovies(): LiveData<List<FavoriteMovie?>?>?

    @Query("SELECT * FROM favorite_movie WHERE id = :id")
    fun loadFavoriteMovieById(id: String?): LiveData<FavoriteMovie?>?

    @Insert
    fun insertFavoriteMovie(favoriteMovie: FavoriteMovie?)

    @Delete
    fun deleteFavoriteMovie(favoriteMovie: FavoriteMovie?)
}