package com.example.okhttp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.okhttp.MovieActivity
import com.example.okhttp.R
import com.example.okhttp.databinding.ListItemBinding
import com.example.okhttp.models.MovieItem

class MovieAdapter: RecyclerView.Adapter<MovieAdapter.HolderMovie> {

    lateinit var binding: ListItemBinding
    val imageUrl = "https://image.tmdb.org/t/p/w500"

    var context: Context
    var movieList: ArrayList<MovieItem>

    constructor(context: Context, movieList: ArrayList<MovieItem>) : super() {
        this.context = context
        this.movieList = movieList
    }

    inner class HolderMovie(itemView: View): RecyclerView.ViewHolder(itemView){
        val title = binding.title
        val description = binding.description
        val image = binding.imageView
        val itemView = binding.itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderMovie {
        binding = ListItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return HolderMovie(binding.root)
    }

    override fun onBindViewHolder(holder: MovieAdapter.HolderMovie, position: Int) {
        val movie = movieList[position]
        val id = movie.id
        val title = movie.title
        val vote_average = movie.vote_average
        val release_date = movie.release_date
        val image = movie.poster_path

        holder.title.text = title
        holder.description.text = "Рейтинг: ${vote_average.toString()}\nПремьера: ${release_date}"

        Glide
            .with(context)
            .load(imageUrl+image)
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.baseline_image_24)
            .into(holder.image)


        holder.itemView.setOnClickListener {
            val intent = Intent(context,MovieActivity::class.java)
            intent.putExtra("id",id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }


}