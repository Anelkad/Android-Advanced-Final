package com.example.okhttp.adapters

import IMAGEURL
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.okhttp.R
import com.example.okhttp.databinding.SavedItemBinding
import com.example.okhttp.models.Movie

class SavedMovieAdapter: ListAdapter<Movie, SavedMovieAdapter.HolderMovie>(DiffCallback()) {

    lateinit var binding: SavedItemBinding

    class HolderMovie(binding: SavedItemBinding): RecyclerView.ViewHolder(binding.root){
        val title = binding.title
        val description = binding.description
        val image = binding.imageView
        val delete = binding.delete
        val itemView = binding.itemView
    }

    class DiffCallback: DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderMovie {
        val binding = SavedItemBinding.inflate(LayoutInflater
            .from(parent.context),parent,false)
        return HolderMovie(binding)
    }

    override fun onBindViewHolder(holder: HolderMovie, position: Int) {
        val movie = getItem(position)
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
        holder.delete.setOnClickListener {deleteMovieListener?.let {it(id)}}
    }

    private var onItemClickListener: ((Int) -> Unit)? = null
    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    private var deleteMovieListener: ((Int) -> Unit)? = null
    fun setDeleteMovieClickListener(listener: (Int) -> Unit){
        deleteMovieListener = listener
    }

}