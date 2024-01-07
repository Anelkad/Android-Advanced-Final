package com.example.okhttp.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.core.utils.ApiConstants.IMAGE_URL
import com.example.okhttp.R
import com.example.okhttp.databinding.SearchItemBinding
import com.example.okhttp.domain.model.Movie

class SearchMovieAdapter(
    private val onItemClickListener: (Int) -> Unit = {},
    private val saveMovieListener: ((Int) -> Unit) = {},
) : ListAdapter<Movie, SearchMovieAdapter.HolderMovie>(DiffCallback()) {
    class HolderMovie(binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.tvTitle
        val description = binding.tvDescription
        val image = binding.ivPoster
        val save = binding.btnSave
        val itemView = binding.itemView
    }

    class DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderMovie {
        val binding = SearchItemBinding.inflate(
            LayoutInflater
                .from(parent.context), parent, false
        )
        return HolderMovie(binding)
    }

    override fun onBindViewHolder(holder: HolderMovie, position: Int) {
        val movie = getItem(position)
        val id = movie.id
        val title = movie.title
        val description = movie.overview
        val voteAverage = movie.voteAverage
        val releaseDate = movie.releaseDate
        val image = movie.posterPath
        val backImage = movie.backdropPath

        holder.title.text = title
        holder.description.text = holder.itemView.context.getString(R.string.premiere, releaseDate)

        Glide
            .with(holder.image.context)
            .load(IMAGE_URL + image)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.baseline_image_24)
            .into(holder.image)


        holder.itemView.setOnClickListener { onItemClickListener.invoke(id) }
        holder.save.setOnClickListener { saveMovieListener.invoke(id) }
    }
}