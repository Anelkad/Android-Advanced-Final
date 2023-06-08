package com.example.okhttp.adapters

import IMAGEURL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.okhttp.R
import com.example.okhttp.databinding.MovieItemBinding
import com.example.okhttp.models.Movie


class MovieAdapter(var movieList: ArrayList<Movie>) :
    RecyclerView.Adapter<MovieAdapter.HolderMovie>() {

    lateinit var binding: MovieItemBinding

    inner class HolderMovie(itemView: View): RecyclerView.ViewHolder(itemView){
        val title = binding.title
        val description = binding.description
        val image = binding.imageView
        val save = binding.save
        val itemView = binding.itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderMovie {
        binding = MovieItemBinding.inflate(LayoutInflater
            .from(parent.context),parent,false)
        return HolderMovie(binding.root)
    }

    override fun onBindViewHolder(holder: MovieAdapter.HolderMovie, position: Int) {
        val movie = movieList[position]
        val id = movie.id
        val title = movie.title
        val description = movie.overview
        val vote_average = movie.vote_average
        val release_date = movie.release_date
        val image = movie.poster_path
        val back_image = movie.backdrop_path


        holder.title.text = title
        holder.description.text = "★ ${vote_average}\nПремьера: ${release_date}"

        Glide
            .with(holder.image.context)
            .load(IMAGEURL+image)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.baseline_image_24)
            .into(holder.image)

        holder.itemView.tag = movie
        holder.itemView.setOnClickListener { onMovieClickListener?.let { it(id) } }

        holder.save.setOnClickListener {saveMovieListener?.let{ it(movie)}}
    }

    override fun getItemCount(): Int {
        return movieList.size
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