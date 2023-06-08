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
import com.example.okhttp.R
import com.example.okhttp.databinding.FragmentMovieDetailsBinding
import com.example.okhttp.models.MovieDetails
import com.example.okhttp.utils.Resource
import com.example.okhttp.viewmodels.MovieDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment: Fragment(R.layout.fragment_movie_details)  {

    lateinit var binding: FragmentMovieDetailsBinding

    val movieViewModel: MovieDetailsViewModel by viewModels()
    val args: MovieDetailsFragmentArgs by navArgs()
    //todo добавить кнопку save movie

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieDetailsBinding.bind(view)

        movieViewModel.movieDetailsDetailsState.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Failure -> {
                    binding.progressBar.isVisible = false
                }
                is Resource.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is Resource.Success -> {
                    binding.progressBar.isVisible = false
                    val movie = it.getSuccessResult()
                    bindMovie(movie)
                }
                else -> Unit
            }
        })

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieViewModel.getMovie(args.id)
    }

    private fun bindMovie(movieDetails: MovieDetails){
        binding.textviewTitle.text = movieDetails.title
        binding.textviewDescription.text = movieDetails.overview
        if (movieDetails.tagline.isNotEmpty()) binding.tagline.text = "\"${movieDetails.tagline}\""
        binding.releaseDate.text = "Премьера: ${movieDetails.release_date}"
        binding.runtime.text = "${movieDetails.runtime/60} ч ${movieDetails.runtime%60} мин"
        if (movieDetails.revenue>0) binding.revenue.text = "Кассовые сборы: ${movieDetails.revenue/1000000} млн $"
        Glide
            .with(this)
            .load(IMAGEURL+movieDetails.poster_path)
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.baseline_image_24)
            .into(binding.imageview)
        Glide
            .with(this)
            .load(IMAGEURL+movieDetails.backdrop_path)
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.baseline_image_24)
            .into(binding.imageview2)
    }
}