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
import com.example.okhttp.SavedMovieListViewModel
import com.example.okhttp.databinding.SavedItemBinding
import com.example.okhttp.models.MovieItem

class SavedMovieAdapter: RecyclerView.Adapter<SavedMovieAdapter.HolderMovie> {

    lateinit var binding: SavedItemBinding
    var movieList: ArrayList<MovieItem>

    var savedMovieListViewModel: SavedMovieListViewModel

    constructor(movieList: ArrayList<MovieItem>, savedMovieListViewModel: SavedMovieListViewModel) : super() {
        this.movieList = movieList
        this.savedMovieListViewModel = savedMovieListViewModel
    }

    inner class HolderMovie(itemView: View): RecyclerView.ViewHolder(itemView){
        val title = binding.title
        val description = binding.description
        val image = binding.imageView
        val delete = binding.delete
        val itemView = binding.itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderMovie {
        binding = SavedItemBinding.inflate(LayoutInflater
            .from(parent.context),parent,false)
        return HolderMovie(binding.root)
    }

    override fun onBindViewHolder(holder: SavedMovieAdapter.HolderMovie, position: Int) {
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


        holder.itemView.setOnClickListener {onItemClickListener?.let { it(id) }}

        holder.delete.setOnClickListener {
            savedMovieListViewModel.deleteMovie(id,holder.delete.context)
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    private var onItemClickListener: ((Int) -> Unit)? = null
    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

}