package com.example.okhttp.fragments

import IMAGEURL
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.okhttp.MovieViewModel
import com.example.okhttp.R
import com.example.okhttp.databinding.FragmentMovieDetailsBinding

class MovieDetailsFragment: Fragment()  {

    lateinit var binding: FragmentMovieDetailsBinding

    val movieViewModel: MovieViewModel by viewModels()

    companion object {
        private val ARG_MOVIE_ID = "ARG_MOVIE_ID"

        fun newInstance(movieId: Int): MovieDetailsFragment{
            val fragment = MovieDetailsFragment()
            fragment.arguments = bundleOf(ARG_MOVIE_ID to movieId)
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieViewModel.fetchMovie(requireArguments().getInt(ARG_MOVIE_ID))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMovieDetailsBinding.inflate(inflater,container,false)

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

        return binding.root
    }
}