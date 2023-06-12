package com.example.okhttp.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.okhttp.R
import com.example.okhttp.models.ListItem
import com.example.okhttp.models.Movie

class PagedMovieAdapter(
    private val onMovieClickListener: ((Int) -> Unit),
    private val saveMovieListener: ((Movie) -> Unit)
) :
    PagingDataAdapter<ListItem, RecyclerView.ViewHolder>(
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
            MovieViewHolder.create(
                parent = parent,
                onMovieClickListener = onMovieClickListener,
                saveMovieListener = saveMovieListener
            )
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
                    //todo Item Decorator
                   }
                is ListItem.AdItem -> (holder as AdViewHolder).bind(listItem.ad)
                else -> {}
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ListItem>() {
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
}