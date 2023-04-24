package com.example.okhttp

import IMAGEURL
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.okhttp.databinding.ActivityMovieBinding

class MovieActivity : AppCompatActivity()  {

    var id: Int = 0
    val movieViewModel: MovieViewModel by viewModels()

    lateinit var binding: ActivityMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getMovieDetails()
        observeViewModel()

    }

    private fun getMovieDetails(){
        id = intent.getIntExtra("id", 0)
        movieViewModel.fetchMovie(id)
    }

    private fun observeViewModel(){
        //observeViewModel()
        movieViewModel.title.observe(this, Observer {
            binding.prgBarMovies.isVisible = true
            binding.textviewTitle.text = it
            binding.prgBarMovies.isVisible = false
        })

        movieViewModel.overview.observe(this, Observer {
            binding.textviewDescription.text = it
        })

        movieViewModel.tagline.observe(this, Observer {
            if(it.isNotEmpty()) binding.tagline.text = "\"${it}\""
        })

        movieViewModel.release_date.observe(this, Observer {
            binding.releaseDate.text = "Премьера: ${it}"
        })

        movieViewModel.runtime.observe(this, Observer {
            binding.runtime.text = "${it/60} ч ${it%60} мин"
        })

        movieViewModel.revenue.observe(this, Observer {
            if (it>0) binding.revenue.text = "Кассовые сборы: ${it/1000000} млн $"
        })

        movieViewModel.poster_path.observe(this, Observer {
            Glide
                .with(this)
                .load(IMAGEURL+it)
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.baseline_image_24)
                .into(binding.imageview);
        })

        movieViewModel.backdrop_path.observe(this, Observer {
            Glide
                .with(this)
                .load(IMAGEURL+it)
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.baseline_image_24)
                .into(binding.imageview2);
        })
    }


}