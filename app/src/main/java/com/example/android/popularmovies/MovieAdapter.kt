package com.example.android.popularmovies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.android.popularmovies.MovieAdapter.MovieViewHolder
import com.example.android.popularmovies.model.Movie
import com.squareup.picasso.Picasso

private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

class MovieAdapter(private val mClickHandler: MovieAdapterOnClickHandler) : RecyclerView.Adapter<MovieViewHolder>() {
    private var movies: List<Movie>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val context = parent.context
        val layoutIdForListItem = R.layout.movie
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(layoutIdForListItem, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val imagePath = IMAGE_BASE_URL + movies!![position].posterPath
        Picasso.get().load(imagePath).into(holder.mImage)
    }

    override fun getItemCount(): Int {
        return if (null != movies) movies!!.size else 0
    }

    fun setMovies(movies: List<Movie>?) {
        this.movies = movies
        notifyDataSetChanged()
    }

    interface MovieAdapterOnClickHandler {
        fun onClick(movie: Movie?, mImage: ImageView)
    }

    inner class MovieViewHolder(itemView: View) : ViewHolder(itemView), View.OnClickListener {
        var mImage: ImageView = itemView.findViewById(R.id.movie_poster)
        override fun onClick(v: View) {
            val adapterPosition = adapterPosition
            val movie = movies!![adapterPosition]
            mClickHandler.onClick(movie, mImage)
        }

        init {
            mImage.setOnClickListener(this)
        }
    }
}