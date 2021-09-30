package com.example.android.popularmovies.utils

import com.example.android.popularmovies.model.Movie
import org.json.JSONException
import org.json.JSONObject
import java.util.*

object JsonUtils {
    private const val ID = "id"
    private const val POSTER_PATH = "poster_path"
    const val RESULTS = "results"
    private const val ORIGINAL_TITLE = "original_title"
    private const val OVERVIEW = "overview"
    private const val VOTE_AVERAGE = "vote_average"
    private const val RELEASE_DATE = "release_date"
    @JvmStatic
    @Throws(JSONException::class)
    fun parseMovieJson(json: JSONObject): List<Movie> {
        val movies: MutableList<Movie> = ArrayList()
        val resultsJsonArray = json.getJSONArray(RESULTS)
        for (i in 0 until resultsJsonArray.length()) {
            val result = resultsJsonArray.getJSONObject(i)
            val id = result.getString(ID)
            val posterPath = result.getString(POSTER_PATH)
            val originalTitle = result.getString(ORIGINAL_TITLE)
            val overview = result.getString(OVERVIEW)
            val voteAverage = result.getDouble(VOTE_AVERAGE)
            val releaseDate = result.getString(RELEASE_DATE)
            movies.add(Movie(id, posterPath, originalTitle, overview, voteAverage, releaseDate))
        }
        return movies
    }
}