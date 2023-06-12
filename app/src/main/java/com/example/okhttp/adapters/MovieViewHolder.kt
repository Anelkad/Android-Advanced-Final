package com.example.okhttp.adapters

import IMAGE_URL
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.okhttp.R
import com.example.okhttp.databinding.MovieItemBinding
import com.example.okhttp.models.Movie

class MovieViewHolder(val movieItemBinding: MovieItemBinding):
    RecyclerView.ViewHolder(movieItemBinding.root) {
    companion object {
        fun create (parent: ViewGroup): MovieViewHolder{
            val binding = MovieItemBinding.inflate(
                LayoutInflater
                    .from(parent.context),parent,false)
            return MovieViewHolder(binding)
        }
    }


    fun bind(movie: Movie) {
            movieItemBinding.title.text = movie.title
            movieItemBinding.description.text = movieItemBinding.description.context.getString(R.string.description,movie.voteAverage.toString(),movie.releaseDate)
            Glide
                .with(movieItemBinding.imageView.context)
                .load(IMAGE_URL+movie.posterPath)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.baseline_image_24)
                .into(movieItemBinding.imageView)

            movieItemBinding.itemView.setOnClickListener {onMovieClickListener?.let {it(movie.id)} }
            movieItemBinding.saveButton.setOnClickListener {saveMovieListener?.let {it(movie)} }
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