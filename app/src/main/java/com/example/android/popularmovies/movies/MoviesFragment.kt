package com.example.android.popularmovies.movies

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.Fade
import android.view.*
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.popularmovies.DetailActivity
import com.example.android.popularmovies.MainViewModel
import com.example.android.popularmovies.MovieAdapter
import com.example.android.popularmovies.R
import com.example.android.popularmovies.database.MoviesApi
import com.example.android.popularmovies.databinding.FragmentMoviesBinding
import com.example.android.popularmovies.model.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

private const val POPULAR = "popular"
private const val TOP_RATED = "top_rated"
private const val FAVORITE_MOVIES = "favorite_movies"
private const val sort_by_key = "sortBy"

class MoviesFragment : Fragment(), MovieAdapter.MovieAdapterOnClickHandler {

    private var movieAdapter: MovieAdapter? = null
    private var mSelectedSortByOption: String? = null

    private var job = Job()
    private var coroutine = CoroutineScope(Dispatchers.Main + job)

    private lateinit var binding: FragmentMoviesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentMoviesBinding.inflate(inflater, container, false)


        val fade = Fade()
        val decor = requireActivity().window.decorView
        fade.excludeTarget(decor.findViewById<View>(R.id.action_bar_container), true)
        fade.excludeTarget(android.R.id.statusBarBackground, true)
        fade.excludeTarget(android.R.id.navigationBarBackground, true)
        requireActivity().window.enterTransition = fade
        requireActivity().window.exitTransition = fade

        val layoutManager = GridLayoutManager(requireActivity(), 2, LinearLayoutManager.VERTICAL, false)
        binding.movies.layoutManager = layoutManager
        movieAdapter = MovieAdapter(this)
        binding.movies.setHasFixedSize(true)
        binding.movies.adapter = movieAdapter
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(sort_by_key)) {
                val sortBy = savedInstanceState
                        .getString(sort_by_key)
                if (sortBy != null) {
                    loadMoviesData(sortBy)
                    mSelectedSortByOption = sortBy
                }
            }
        } else {
            loadMoviesData(POPULAR)
            mSelectedSortByOption = POPULAR
        }

        setHasOptionsMenu(true)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onClick(movie: Movie?, mImage: ImageView) {
        val intentToStartDetailActivity = Intent(requireActivity(), DetailActivity::class.java)
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, movie)

        val options = ActivityOptions.makeSceneTransitionAnimation(
                requireActivity(),
                mImage,
                "shared_element_container" // The transition name to be matched in Activity B.
        )

        startActivity(intentToStartDetailActivity, options.toBundle())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.movies, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_sort_popularity) {
            loadMoviesData(POPULAR)
            mSelectedSortByOption = POPULAR
            return true
        }
        if (id == R.id.action_sort_rating) {
            loadMoviesData(TOP_RATED)
            mSelectedSortByOption = TOP_RATED
            return true
        }
        if (id == R.id.favorite_movies) {
            loadMoviesData(FAVORITE_MOVIES)
            mSelectedSortByOption = FAVORITE_MOVIES
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(sort_by_key, mSelectedSortByOption)
    }

    private fun loadMoviesData(sortBy: String) {
//        if (isOnline) {
        binding.pbLoadingIndicator.visibility = View.VISIBLE
        binding.movies.visibility = View.INVISIBLE
        binding.errorMessageDisplay.visibility = View.INVISIBLE
        if (FAVORITE_MOVIES == sortBy) {
            loadFavoriteMovies()
        } else {
            coroutine.launch {
                val properties = MoviesApi.retrofitService.getPropertiesAsync(sortBy, "5ebf4e990d91bf45fff6189ad0b5b7ed")
                try {
                    var listResult = properties.await()
                    for (it in listResult.results) {
                        val (id, posterPath, overview) = it
                        println("test id $overview")
                    }
                    movieAdapter!!.setMovies(listResult.results)
                    binding.pbLoadingIndicator.visibility = View.INVISIBLE
                    binding.movies.visibility = View.VISIBLE
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
//        } else {
//            binding.errorMessageDisplay.visibility = View.VISIBLE
//        }
        }
    }

    private fun loadFavoriteMovies() {
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.favoriteMovies?.observe(requireActivity(), { favoriteMovies ->
            val movies: MutableList<Movie> = ArrayList()
            for (favoriteMovie in favoriteMovies!!) {
                if (favoriteMovie != null) {
                    movies.add(Movie(favoriteMovie.id.toString(), favoriteMovie.posterPath, favoriteMovie.originalTitle, favoriteMovie.overview, favoriteMovie.voteAverage, favoriteMovie.releaseDate))
                }
            }
            movieAdapter!!.setMovies(movies)
            binding.pbLoadingIndicator.visibility = View.INVISIBLE
            binding.movies.visibility = View.VISIBLE
        })
    }

    // TODO: Fix the error
//    private val isOnline: Boolean
//        get() {
//            val cm = getSystemService(requireActivity(), AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
//            val netInfo = cm.activeNetworkInfo
//            return netInfo != null && netInfo.isConnected
//        }
}