package com.example.android.popularmovies.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movie")
class FavoriteMovie(@PrimaryKey var id: Int, val posterPath: String, val originalTitle: String, val overview: String, val voteAverage: Double, val releaseDate: String?)