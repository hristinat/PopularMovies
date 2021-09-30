package com.example.android.popularmovies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.android.popularmovies.TrailerAdapter.TrailerViewHolder

class TrailerAdapter(private val mClickHandler: TrailerAdapterOnClickHandler) : RecyclerView.Adapter<TrailerViewHolder>() {
    private var trailerKeys: List<String>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerViewHolder {
        val context = parent.context
        val layoutIdForListItem = R.layout.trailer
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(layoutIdForListItem, parent, false)
        return TrailerViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrailerViewHolder, position: Int) {
        val trailerNumber = position + 1
        holder.mTrailerText.text = "Trailer $trailerNumber"
    }

    override fun getItemCount(): Int {
        return if (null != trailerKeys) trailerKeys!!.size else 0
    }

    fun setTrailerKeys(trailerKeys: List<String>?) {
        this.trailerKeys = trailerKeys
        notifyDataSetChanged()
    }

    interface TrailerAdapterOnClickHandler {
        fun onClick(trailerKey: String?)
    }

    inner class TrailerViewHolder(itemView: View) : ViewHolder(itemView), View.OnClickListener {
        var mImage: ImageView
        var mTrailerText: TextView
        override fun onClick(v: View) {
            val adapterPosition = adapterPosition
            val trailerKey = trailerKeys!![adapterPosition]
            mClickHandler.onClick(trailerKey)
        }

        init {
            mImage = itemView.findViewById(R.id.play_arrow)
            mImage.setOnClickListener(this)
            mTrailerText = itemView.findViewById(R.id.trailer_text)
        }
    }
}