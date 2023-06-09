package com.example.okhttp.adapters

import IMAGEURL
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.okhttp.R
import com.example.okhttp.databinding.MovieItemBinding
import com.example.okhttp.models.Movie

class PagedMovieAdapter: PagingDataAdapter<Movie, PagedMovieAdapter.MovieViewHolder>(
    DiffCallback()
) {
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie: Movie = getItem(position)!!

        with (holder.movieItemBinding){
            title.text = movie.title
            description.text = "★ ${movie.vote_average}\nПремьера: ${movie.release_date}"

            Glide
                .with(imageView.context)
                .load(IMAGEURL+movie.poster_path)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.baseline_image_24)
                .into(imageView)

            itemView.setOnClickListener { onMovieClickListener?.let { it(movie.id) } }
            save.setOnClickListener {saveMovieListener?.let{ it(movie)}}

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MovieItemBinding.inflate(
            LayoutInflater
            .from(parent.context),parent,false)
        return MovieViewHolder(binding)
    }

    class DiffCallback: DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
    }

    class MovieViewHolder(val movieItemBinding: MovieItemBinding) :
        RecyclerView.ViewHolder(movieItemBinding.root)

    private var onMovieClickListener: ((Int) -> Unit)? = null
    fun setOnMovieClickListener(listener: (Int) -> Unit) {
        onMovieClickListener = listener
    }

    private var saveMovieListener: ((Movie) -> Unit)? = null
    fun setSaveMovieClickListener(listener: (Movie) -> Unit){
        saveMovieListener = listener
    }
}