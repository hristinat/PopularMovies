package com.example.android.popularmovies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.android.popularmovies.ReviewAdapter.ReviewViewHolder
import com.example.android.popularmovies.model.Review

class ReviewAdapter : RecyclerView.Adapter<ReviewViewHolder>() {
    private var reviews: List<Review>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val context = parent.context
        val layoutIdForListItem = R.layout.review
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(layoutIdForListItem, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews!![position]
        holder.mAuthor.text = review.author
        holder.mContent.text = review.content
    }

    override fun getItemCount(): Int {
        return if (null != reviews) reviews!!.size else 0
    }

    fun setReviews(reviews: List<Review>?) {
        this.reviews = reviews
        notifyDataSetChanged()
    }

    inner class ReviewViewHolder(itemView: View) : ViewHolder(itemView) {
        var mAuthor: TextView = itemView.findViewById(R.id.author)
        var mContent: TextView = itemView.findViewById(R.id.content)
    }
}