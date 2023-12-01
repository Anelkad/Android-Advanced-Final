package com.example.okhttp.movieList

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.okhttp.R
import com.example.domain.model.ListItem
import com.example.domain.model.Movie

class PagedMovieAdapter(
    private val onMovieClickListener: ((Int) -> Unit),
    private val saveMovieListener: ((com.example.domain.model.Movie) -> Unit)
) :
    PagingDataAdapter<com.example.domain.model.ListItem, RecyclerView.ViewHolder>(
        DiffCallback()
    ) {
    override fun getItemViewType(position: Int): Int {
        return when (peek(position)) {
            is com.example.domain.model.ListItem.MovieItem -> R.layout.movie_item
            is com.example.domain.model.ListItem.AdItem -> R.layout.ad_item
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
                is com.example.domain.model.ListItem.MovieItem -> {
                    val movieHolder = (holder as MovieViewHolder)
                    movieHolder.bind(listItem.movie)
                    //todo Item Decorator
                   }
                is com.example.domain.model.ListItem.AdItem -> (holder as AdViewHolder).bind(listItem.ad)
                else -> {}
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<com.example.domain.model.ListItem>() {
        override fun areItemsTheSame(oldItem: com.example.domain.model.ListItem, newItem: com.example.domain.model.ListItem): Boolean {
            val isSameMovieItem = oldItem is com.example.domain.model.ListItem.MovieItem
                    && newItem is com.example.domain.model.ListItem.MovieItem
                    && oldItem.movie.id == newItem.movie.id

            val isSameAdItem = oldItem is com.example.domain.model.ListItem.AdItem
                    && newItem is com.example.domain.model.ListItem.AdItem
                    && oldItem.ad == newItem.ad

            return isSameMovieItem || isSameAdItem
        }

        override fun areContentsTheSame(oldItem: com.example.domain.model.ListItem, newItem: com.example.domain.model.ListItem) = oldItem == newItem
    }
}