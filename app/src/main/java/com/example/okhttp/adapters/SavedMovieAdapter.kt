package com.example.okhttp.adapters

import IMAGEURL
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.okhttp.MovieActivity
import com.example.okhttp.R
import com.example.okhttp.SavedMovieListViewModel
import com.example.okhttp.databinding.SavedItemBinding
import com.example.okhttp.models.MovieItem
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel

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
        holder.description.text = "Рейтинг: ${vote_average.toString()}\nПремьера: ${release_date}"

        Glide
            .with(holder.image.context)
            .load(IMAGEURL+image)
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.baseline_image_24)
            .into(holder.image)


        holder.itemView.setOnClickListener {
            //bottom nav doesn't respond
//            val activity  = it.context as? AppCompatActivity
//
//            activity?.supportFragmentManager?.beginTransaction()
//                ?.addToBackStack(null)
//                ?.replace(R.id.fragmentContainer,MovieDetailsFragment.newInstance(id))
//                ?.commit()

            val intent = Intent(holder.itemView.context, MovieActivity::class.java)
            intent.putExtra("id",id)
            holder.itemView.context.startActivity(intent)
        }

        holder.delete.setOnClickListener {
            savedMovieListViewModel.deleteMovie(id,holder.delete.context)
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }


}