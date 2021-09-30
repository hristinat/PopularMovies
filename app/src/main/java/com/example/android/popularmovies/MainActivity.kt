package com.example.android.popularmovies

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    //    private var mMoviesList: RecyclerView? = null
//    private var movieAdapter: MovieAdapter? = null
//    private var mLoadingIndicator: ProgressBar? = null
//    private var mErrorMessageDisplay: TextView? = null
//    private var movies: List<Movie>? = null
//    private var mSelectedSortByOption: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        mMoviesList = findViewById(R.id.movies)
//        mLoadingIndicator = findViewById(R.id.pb_loading_indicator)
//        mErrorMessageDisplay = findViewById(R.id.error_message_display)
//        val layoutManager = GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false)
//        mMoviesList?.layoutManager = layoutManager
//        movieAdapter = MovieAdapter(this)
//        mMoviesList?.setHasFixedSize(true)
//        mMoviesList?.adapter = movieAdapter
//        if (savedInstanceState != null) {
//            if (savedInstanceState.containsKey(sort_by_key)) {
//                val sortBy = savedInstanceState
//                        .getString(sort_by_key)
//                loadMoviesData(sortBy)
//                mSelectedSortByOption = sortBy
//            }
//        } else {
//            loadMoviesData(POPULAR)
//            mSelectedSortByOption = POPULAR
//        }
    }
//
//    override fun onClick(movie: Movie?) {
//        val intentToStartDetailActivity = Intent(this, DetailActivity::class.java)
//        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, movie)
//        startActivity(intentToStartDetailActivity)
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.movies, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id = item.itemId
//        if (id == R.id.action_sort_popularity) {
//            loadMoviesData(POPULAR)
//            mSelectedSortByOption = POPULAR
//            return true
//        }
//        if (id == R.id.action_sort_rating) {
//            loadMoviesData(TOP_RATED)
//            mSelectedSortByOption = TOP_RATED
//            return true
//        }
//        if (id == R.id.favorite_movies) {
//            loadMoviesData(FAVORITE_MOVIES)
//            mSelectedSortByOption = FAVORITE_MOVIES
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putString(sort_by_key, mSelectedSortByOption)
//    }
//
//    private fun loadMoviesData(sortBy: String?) {
//        if (isOnline) {
//            mLoadingIndicator!!.visibility = View.VISIBLE
//            mMoviesList!!.visibility = View.INVISIBLE
//            mErrorMessageDisplay!!.visibility = View.INVISIBLE
//            if (FAVORITE_MOVIES == sortBy) {
//                loadFavoriteMovies()
//            } else {
//                val url = MovieApiUrls.MOVIE_API_BASE_URL + sortBy + "?api_key=5ebf4e990d91bf45fff6189ad0b5b7ed"
//                val queue = Volley.newRequestQueue(this)
//                val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response ->
//                    try {
//                        movies = parseMovieJson(response!!)
//                        movieAdapter!!.setMovies(movies)
//                        mLoadingIndicator!!.visibility = View.INVISIBLE
//                        mMoviesList!!.visibility = View.VISIBLE
//                    } catch (e: JSONException) {
//                        e.printStackTrace()
//                    }
//                }) { error -> error.printStackTrace() }
//                queue.add(jsonObjectRequest)
//            }
//        } else {
//            mErrorMessageDisplay!!.visibility = View.VISIBLE
//        }
//    }
//
//    private fun loadFavoriteMovies() {
//        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
//        viewModel.favoriteMovies?.observe(this, { favoriteMovies ->
//            val movies: MutableList<Movie> = ArrayList()
//            for (favoriteMovie in favoriteMovies!!) {
//                if (favoriteMovie != null) {
//                    movies.add(Movie(favoriteMovie?.id.toString(), favoriteMovie.posterPath, favoriteMovie.originalTitle, favoriteMovie.overview, favoriteMovie.voteAverage, favoriteMovie.releaseDate))
//                }
//            }
//            movieAdapter!!.setMovies(movies)
//            mLoadingIndicator!!.visibility = View.INVISIBLE
//            mMoviesList!!.visibility = View.VISIBLE
//        })
//    }
//
//    private val isOnline: Boolean
//        private get() {
//            val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
//            val netInfo = cm.activeNetworkInfo
//            return netInfo != null && netInfo.isConnected
//        }
}