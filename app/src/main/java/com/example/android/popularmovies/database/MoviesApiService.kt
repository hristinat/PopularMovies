package com.example.android.popularmovies.database

import com.example.android.popularmovies.model.Movie
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val MOVIE_API_BASE_URL = "https://api.themoviedb.org/3/movie/"
var moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
private var retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(MOVIE_API_BASE_URL).build()

interface MoviesApiService {
    @GET("{sortBy}")
    fun getPropertiesAsync(@Path("sortBy") sortBy: String, @Query("api_key") apiKey: String): Deferred<Response>
}

object MoviesApi {
    val retrofitService: MoviesApiService by lazy {
        retrofit.create(MoviesApiService::class.java)
    }
}

data class Response(var page: String, var results: List<Movie>)