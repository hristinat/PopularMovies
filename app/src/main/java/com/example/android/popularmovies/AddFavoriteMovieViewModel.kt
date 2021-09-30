package com.example.android.popularmovies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.android.popularmovies.database.AppDatabase
import com.example.android.popularmovies.database.FavoriteMovie

class AddFavoriteMovieViewModel(database: AppDatabase, favoriteMovieId: String?) : ViewModel() {
    val favoriteMovie: LiveData<FavoriteMovie?>? = database.favoriteMovieDao()!!.loadFavoriteMovieById(favoriteMovieId)
}