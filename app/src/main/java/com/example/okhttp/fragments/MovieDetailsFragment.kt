package com.example.okhttp.fragments

import IMAGEURL
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
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

        movieViewModel.movie.observe(viewLifecycleOwner, Observer {
            binding.prgBarMovies.isVisible = true
            if (it != null) {
                binding.prgBarMovies.isVisible = false
                binding.textviewTitle.text = it.title
                binding.textviewDescription.text = it.overview
                if (it.tagline.isNotEmpty()) binding.tagline.text = "\"${it.tagline}\""
                binding.releaseDate.text = "Премьера: ${it.release_date}"
                binding.runtime.text = "${it.runtime/60} ч ${it.runtime%60} мин"
                if (it.revenue>0) binding.revenue.text = "Кассовые сборы: ${it.revenue/1000000} млн $"
                Glide
                    .with(this)
                    .load(IMAGEURL+it.poster_path)
                    .placeholder(R.drawable.progress_animation)
                    .error(R.drawable.baseline_image_24)
                    .into(binding.imageview)
                Glide
                    .with(this)
                    .load(IMAGEURL+it.backdrop_path)
                    .placeholder(R.drawable.progress_animation)
                    .error(R.drawable.baseline_image_24)
                    .into(binding.imageview2);
            }
            })

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieViewModel.fetchMovie(args.id)
    }
}