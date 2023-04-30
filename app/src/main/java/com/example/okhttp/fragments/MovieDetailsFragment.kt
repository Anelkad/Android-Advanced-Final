package com.example.okhttp.fragments

import IMAGEURL
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.okhttp.MovieViewModel
import com.example.okhttp.R
import com.example.okhttp.databinding.FragmentMovieDetailsBinding

class MovieDetailsFragment: Fragment(R.layout.fragment_movie_details)  {

    lateinit var binding: FragmentMovieDetailsBinding

    val movieViewModel: MovieViewModel by viewModels()
    val args: MovieDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieDetailsBinding.bind(view)

        movieViewModel.title.observe(viewLifecycleOwner, Observer {
            binding.prgBarMovies.isVisible = true
            binding.textviewTitle.text = it
            binding.prgBarMovies.isVisible = false
        })

        movieViewModel.overview.observe(viewLifecycleOwner, Observer {
            binding.textviewDescription.text = it
        })

        movieViewModel.tagline.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()) binding.tagline.text = "\"${it}\""
        })

        movieViewModel.release_date.observe(viewLifecycleOwner, Observer {
            binding.releaseDate.text = "Премьера: ${it}"
        })

        movieViewModel.runtime.observe(viewLifecycleOwner, Observer {
            binding.runtime.text = "${it/60} ч ${it%60} мин"
        })

        movieViewModel.revenue.observe(viewLifecycleOwner, Observer {
            if (it>0) binding.revenue.text = "Кассовые сборы: ${it/1000000} млн $"
        })

        movieViewModel.poster_path.observe(viewLifecycleOwner, Observer {
            Glide
                .with(this)
                .load(IMAGEURL+it)
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.baseline_image_24)
                .into(binding.imageview);
        })

        movieViewModel.backdrop_path.observe(viewLifecycleOwner, Observer {
            Glide
                .with(this)
                .load(IMAGEURL+it)
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.baseline_image_24)
                .into(binding.imageview2);
        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieViewModel.fetchMovie(args.id)
    }
}