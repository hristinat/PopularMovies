package com.example.android.popularmovies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.example.android.popularmovies.database.AppDatabase

@Suppress("UNCHECKED_CAST")
class AddFavoriteMovieViewModelFactory(private val mDb: AppDatabase, private val mFavoriteMovieId: String) : NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddFavoriteMovieViewModel(mDb, mFavoriteMovieId) as T
    }
}