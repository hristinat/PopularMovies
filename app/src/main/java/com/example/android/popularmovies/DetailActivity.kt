package com.example.android.popularmovies

import android.R.attr.targetActivity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.transition.Fade
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.android.popularmovies.AppExecutors.Companion.instance
import com.example.android.popularmovies.TrailerAdapter.TrailerAdapterOnClickHandler
import com.example.android.popularmovies.database.AppDatabase
import com.example.android.popularmovies.database.AppDatabase.Companion.getInstance
import com.example.android.popularmovies.database.FavoriteMovie
import com.example.android.popularmovies.model.Movie
import com.example.android.popularmovies.model.Review
import com.example.android.popularmovies.utils.JsonUtils
import com.example.android.popularmovies.utils.MovieApiUrls
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import org.json.JSONException
import java.lang.Exception
import java.util.*


class DetailActivity : AppCompatActivity(), TrailerAdapterOnClickHandler {
    var mMovieTitle: TextView? = null
    var mMoviePoster: ImageView? = null
    var mReleaseDate: TextView? = null
    var mOverview: TextView? = null
    var mVoteAverage: TextView? = null
    var mTrailersNotAvailable: TextView? = null
    var mReviewsNotAdded: TextView? = null
    var mMovieId: String? = null
    var trailersKeys: MutableList<String> = ArrayList()
    var reviews: MutableList<Review> = ArrayList()
    var mTrailersList: RecyclerView? = null
    var trailerAdapter: TrailerAdapter? = null
    var mReviewsList: RecyclerView? = null
    var reviewsAdapter: ReviewAdapter? = null
    var mMarkAsFavoriteButton: Button? = null
    private var mDb: AppDatabase? = null
    var favoriteMovie: FavoriteMovie? = null
    var isAddedAsFavorite = false

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

//        ActivityCompat.postponeEnterTransition(this)

        val fade = Fade()
        val decor = window.decorView
        fade.excludeTarget(decor.findViewById<View>(R.id.action_bar_container), true)
        fade.excludeTarget(android.R.id.statusBarBackground, true)
        fade.excludeTarget(android.R.id.navigationBarBackground, true)
        window.enterTransition = fade
        window.exitTransition = fade

//        val fade = Fade()
//        val decor = window.decorView
////        fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true)
//        fade.excludeTarget(R.id.statusBarBackground, true)
//        fade.excludeTarget(R.id.navigationBarBackground, true)
//        window.enterTransition = fade
//        window.exitTransition = fade


//        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
//        // Set this Activity’s enter and return transition to a MaterialContainerTransform
//        window.sharedElementEnterTransition = MaterialContainerTransform().apply {
//            addTarget(R.id.movie_poster_thumbnail)
//            startContainerColor = Color.WHITE
//            endContainerColor = Color.WHITE
//            containerColor = Color.WHITE
//            duration = 300L
//        }
//
//        window.sharedElementReturnTransition = MaterialContainerTransform().apply {
//            addTarget(R.id.movie_poster_thumbnail)
//            startContainerColor = Color.WHITE
//            endContainerColor = Color.WHITE
//            containerColor = Color.WHITE
//            duration = 250L
//        }

        initViews()
        mDb = getInstance(applicationContext)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mTrailersList!!.layoutManager = layoutManager
        trailerAdapter = TrailerAdapter(this)
        mTrailersList!!.setHasFixedSize(true)
        mTrailersList!!.adapter = trailerAdapter
        val reviewsLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mReviewsList!!.layoutManager = reviewsLayoutManager
        reviewsAdapter = ReviewAdapter()
        mReviewsList!!.setHasFixedSize(true)
        mReviewsList!!.adapter = reviewsAdapter
        val intentThatStartedThisActivity = intent
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                val movie = intentThatStartedThisActivity.getSerializableExtra(Intent.EXTRA_TEXT) as Movie
                mMovieId = movie.id
                mMovieTitle!!.text = movie.originalTitle
                mReleaseDate!!.text = movie.releaseDate
                mOverview!!.text = movie.overview
                mVoteAverage!!.text = getString(R.string.vote_average, movie.voteAverage.toString())
                val imagePath = MovieApiUrls.IMAGE_BASE_URL + movie.posterPath
                mMoviePoster?.let {
                    Glide.with(this)
                            .load(imagePath)
                            .into(it)
                }
//                Picasso.get().load(image_path).noFade().into(mMoviePoster,
//                        object : Callback {
//                            override fun onSuccess() {
//                                ActivityCompat.startPostponedEnterTransition(this@DetailActivity)
//                            }
//
//                            override fun onError(e: Exception?) {
//                                ActivityCompat.startPostponedEnterTransition(this@DetailActivity);
//                            }
//                        })
                favoriteMovie = FavoriteMovie(Integer.valueOf(mMovieId!!), movie.posterPath, movie.originalTitle, movie.overview, movie.voteAverage, movie.releaseDate)
                val queue = Volley.newRequestQueue(this)
                val jsonObjectRequest = trailersJsonObjectRequest
                val reviewsRequest = reviewsJsonObjectRequest
                queue.add(jsonObjectRequest)
                queue.add(reviewsRequest)
                setIsAddedAsFavorite()
            }
        }
    }

    override fun onClick(trailerKey: String?) {
        val intent = Intent(Intent.ACTION_VIEW,
                Uri.parse(YOUTUBE_LINK + trailerKey))
        startActivity(intent)
    }

    fun addOrDeleteFavoriteMovie(view: View) {
        instance!!.diskIO().execute {
            if (isAddedAsFavorite) {
                mDb!!.favoriteMovieDao()!!.deleteFavoriteMovie(favoriteMovie)
            } else {
                mDb!!.favoriteMovieDao()!!.insertFavoriteMovie(favoriteMovie)
            }
        }
    }

    private fun setIsAddedAsFavorite() {
        val factory = AddFavoriteMovieViewModelFactory(mDb!!, mMovieId!!)
        val viewModel = ViewModelProvider(this, factory).get(AddFavoriteMovieViewModel::class.java)
        viewModel.favoriteMovie!!.observe(this, { favoriteMovie ->
            isAddedAsFavorite = favoriteMovie != null
            if (isAddedAsFavorite) {
                mMarkAsFavoriteButton!!.setBackgroundResource(R.drawable.ic_baseline_favorite_24)
            } else {
                mMarkAsFavoriteButton!!.setBackgroundResource(R.drawable.ic_outline_favorite_border_24)
            }
        })
    }

    private fun initViews() {
        mMovieTitle = findViewById(R.id.movie_title)
        mReleaseDate = findViewById(R.id.release_date)
        mOverview = findViewById(R.id.overview)
        mVoteAverage = findViewById(R.id.vote_average)
        mMoviePoster = findViewById(R.id.movie_poster_thumbnail)
        mTrailersList = findViewById(R.id.trailers)
        mReviewsList = findViewById(R.id.reviews)
        mMarkAsFavoriteButton = findViewById(R.id.mark_as_favorite)
        mTrailersNotAvailable = findViewById(R.id.trailer_not_available)
        mReviewsNotAdded = findViewById(R.id.reviews_not_added)
    }

    private val trailersJsonObjectRequest: JsonObjectRequest
        get() {
            val trailersUrl = MovieApiUrls.MOVIE_API_BASE_URL + mMovieId + "/videos?api_key=5ebf4e990d91bf45fff6189ad0b5b7ed"
            return JsonObjectRequest(Request.Method.GET, trailersUrl, null, { response ->
                try {
                    val resultsJsonArray = response.getJSONArray(JsonUtils.RESULTS)
                    for (i in 0 until resultsJsonArray.length()) {
                        val result = resultsJsonArray.getJSONObject(i)
                        trailersKeys.add(result.getString(KEY))
                    }
                    trailerAdapter!!.setTrailerKeys(trailersKeys)
                    if (trailersKeys.size == 0) {
                        mTrailersNotAvailable!!.visibility = View.VISIBLE
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        }
    private val reviewsJsonObjectRequest: JsonObjectRequest
        get() {
            val reviewsUrl = MovieApiUrls.MOVIE_API_BASE_URL + mMovieId + "/reviews?api_key=5ebf4e990d91bf45fff6189ad0b5b7ed"
            return JsonObjectRequest(Request.Method.GET, reviewsUrl, null, { response ->
                try {
                    val resultsJsonArray = response.getJSONArray(JsonUtils.RESULTS)
                    for (i in 0 until resultsJsonArray.length()) {
                        val result = resultsJsonArray.getJSONObject(i)
                        reviews.add(Review(result.getString(AUTHOR), result.getString(CONTENT)))
                    }
                    reviewsAdapter!!.setReviews(reviews)
                    if (reviews.size == 0) {
                        mReviewsNotAdded!!.visibility = View.VISIBLE
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        }

    companion object {
        private const val YOUTUBE_LINK = "https://www.youtube.com/watch?v="
        private const val KEY = "key"
        private const val AUTHOR = "author"
        private const val CONTENT = "content"
    }
}