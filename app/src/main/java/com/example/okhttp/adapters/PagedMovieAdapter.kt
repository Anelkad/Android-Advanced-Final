package com.example.okhttp.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.okhttp.R
import com.example.okhttp.models.ListItem
import com.example.okhttp.models.Movie

class PagedMovieAdapter: PagingDataAdapter<ListItem, RecyclerView.ViewHolder>(
    DiffCallback()
) {
    override fun getItemViewType(position: Int): Int {
        return when (peek(position)) {
            is ListItem.MovieItem -> R.layout.movie_item
            is ListItem.AdItem -> R.layout.ad_item
            null -> throw IllegalStateException("Unknown view")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.movie_item) {
            MovieViewHolder.create(parent)
        } else {
            AdViewHolder.create(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val listItem = getItem(position)
        listItem.let {
            when (listItem) {
                is ListItem.MovieItem -> {
                    val movieHolder = (holder as MovieViewHolder)
                    movieHolder.bind(listItem.movie)
                    //todo listeter-ы дублируются
                    movieHolder.setOnMovieClickListener {onMovieClickListener?.let {it(listItem.movie.id)}}
                    movieHolder.setSaveMovieClickListener {saveMovieListener?.let{ it(listItem.movie)}}
                }
                is ListItem.AdItem -> (holder as AdViewHolder).bind(listItem.ad)
                else -> {}
            }
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<ListItem>() {
        override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            val isSameMovieItem = oldItem is ListItem.MovieItem
                    && newItem is ListItem.MovieItem
                    && oldItem.movie.id == newItem.movie.id

            val isSameAdItem = oldItem is ListItem.AdItem
                    && newItem is ListItem.AdItem
                    && oldItem.ad == newItem.ad

            return isSameMovieItem || isSameAdItem
        }
        override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem) = oldItem == newItem
    }

    private var onMovieClickListener: ((Int) -> Unit)? = null
    fun setOnMovieClickListener(listener: (Int) -> Unit) {
        onMovieClickListener = listener
    }

    private var saveMovieListener: ((Movie) -> Unit)? = null
    fun setSaveMovieClickListener(listener: (Movie) -> Unit){
        saveMovieListener = listener
    }
}