package com.example.android.popularmovies

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.android.popularmovies.database.AppDatabase.Companion.getInstance
import com.example.android.popularmovies.database.FavoriteMovie

class MainViewModel(application: Application?) : AndroidViewModel(application!!) {
    val favoriteMovies: LiveData<List<FavoriteMovie?>?>?

    init {
        val database = getInstance(getApplication())
        favoriteMovies = database!!.favoriteMovieDao()!!.loadAllFavoriteMovies()
    }
}