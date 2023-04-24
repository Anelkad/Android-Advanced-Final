package com.example.okhttp.adapters

import IMAGEURL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.okhttp.R
import com.example.okhttp.databinding.ListItemBinding
import com.example.okhttp.fragments.MovieDetailsFragment
import com.example.okhttp.fragments.MovieListFragment
import com.example.okhttp.models.MovieItem

class MovieAdapter: RecyclerView.Adapter<MovieAdapter.HolderMovie> {

    lateinit var binding: ListItemBinding
    var movieList: ArrayList<MovieItem>

    constructor(movieList: ArrayList<MovieItem>) : super() {
        this.movieList = movieList
    }

    inner class HolderMovie(itemView: View): RecyclerView.ViewHolder(itemView){
        val title = binding.title
        val description = binding.description
        val image = binding.imageView
        val itemView = binding.itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderMovie {
        binding = ListItemBinding.inflate(LayoutInflater
            .from(parent.context),parent,false)

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
            .with(holder.image.context)
            .load(IMAGEURL+image)
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.baseline_image_24)
            .into(holder.image)


        holder.itemView.setOnClickListener {
            //TODO
            val activity  = it.context as? AppCompatActivity

            activity?.supportFragmentManager?.beginTransaction()
                ?.addToBackStack(null)
                ?.replace(R.id.fragmentContainer,MovieDetailsFragment.newInstance(id))
                ?.commit()


//            val intent = Intent(holder.itemView.context,MovieActivity::class.java)
//            intent.putExtra("id",id)
//            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }


}