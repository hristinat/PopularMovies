package com.example.android.popularmovies.model

import com.squareup.moshi.Json
import java.io.Serializable

data class Movie(val id: String, @Json(name = "poster_path") val posterPath: String,
            @Json(name = "original_title") val originalTitle: String, val overview: String,
            @Json(name = "vote_average") val voteAverage: Double, @Json(name = "release_date") val releaseDate: String?) : Serializable