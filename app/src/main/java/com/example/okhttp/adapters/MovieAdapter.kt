package com.example.okhttp.adapters

import IMAGEURL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.okhttp.R
import com.example.okhttp.SavedMovieListViewModel
import com.example.okhttp.databinding.MovieItemBinding
import com.example.okhttp.models.MovieItem


class MovieAdapter: RecyclerView.Adapter<MovieAdapter.HolderMovie>{

    lateinit var binding: MovieItemBinding
    var movieList: ArrayList<MovieItem>

    var savedMovieListViewModel: SavedMovieListViewModel

    constructor(movieList: ArrayList<MovieItem>, movieListViewModel: SavedMovieListViewModel) : super() {
        this.movieList = movieList
        //todo lambda function чтобы не передавать в конструктор ViewModel
        this.savedMovieListViewModel = movieListViewModel
    }

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
        holder.description.text = "Рейтинг: ${vote_average.toString()}\nПремьера: ${release_date}"

        Glide
            .with(holder.image.context)
            .load(IMAGEURL+image)
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.baseline_image_24)
            .into(holder.image)

        holder.itemView.tag = movie
        holder.itemView.setOnClickListener { onItemClickListener?.let { it(id) } }

        holder.save.setOnClickListener {
            val movie = MovieItem(
                id,
                title,
                description,
                release_date,
                image,
                back_image,
                vote_average
            )
            savedMovieListViewModel.addMovie(movie,holder.save.context)
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