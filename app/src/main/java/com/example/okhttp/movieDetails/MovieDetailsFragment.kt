package com.example.okhttp.movieDetails

import IMAGE_URL
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.okhttp.R
import com.example.okhttp.databinding.FragmentMovieDetailsBinding
import com.example.okhttp.delegates.DialogDelegate
import com.example.okhttp.delegates.WaitDialogDelegate
import com.example.okhttp.models.MovieDetails
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details),
    DialogDelegate by WaitDialogDelegate() {

    private var binding: FragmentMovieDetailsBinding? = null
    private val movieViewModel: MovieDetailsViewModel by viewModels()
    private val args: MovieDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieDetailsBinding.bind(view)
        movieViewModel.getMovie(args.id)
        bindViews()
        setupObservers()
        registerWaitDialogDelegate(this)
    }

    private fun bindViews() {
        binding?.backButton?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupObservers() {
        movieViewModel.state.onEach { state ->
            when (state) {
                is MovieDetailsViewModel.State.Error -> {
                    Toast.makeText(
                        context,
                        requireContext().getString(R.string.smth_went_wrong),
                        Toast.LENGTH_LONG
                    ).show()
                }

                is MovieDetailsViewModel.State.HideLoading -> {
                    binding?.progressBar?.isVisible = false
                }

                is MovieDetailsViewModel.State.ShowLoading -> {
                    binding?.progressBar?.isVisible = true
                }

                is MovieDetailsViewModel.State.ShowMovieDetails -> {
                    bindMovie(state.movie)
                }

                is MovieDetailsViewModel.State.MovieSaved -> {
                    Toast.makeText(
                        context,
                        requireContext().getString(R.string.movie_saved_title, state.movie.title),
                        Toast.LENGTH_LONG
                    ).show()
                }

                is MovieDetailsViewModel.State.ShowWaitDialog -> {
                    showWaitDialog()
                }

                is MovieDetailsViewModel.State.HideWaitDialog -> {
                    hideWaitDialog()
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun bindMovie(movieDetails: MovieDetails) {
        binding?.apply {
            textviewTitle.text = movieDetails.title
            textviewDescription.text = movieDetails.overview
            if (movieDetails.tagline.isNotEmpty()) tagline.text =
                tagline.context.getString(R.string.tagline, movieDetails.tagline)
            releaseDate.text =
                releaseDate.context.getString(R.string.premiere, movieDetails.releaseDate)
            runtime.text = runtime.context.getString(
                R.string.runtime,
                movieDetails.runtime / 60,
                movieDetails.runtime % 60
            )
            if (movieDetails.revenue > 0) revenue.text =
                revenue.context.getString(R.string.revenue, movieDetails.revenue / 1000000)

            Glide
                .with(imageview.context)
                .load(IMAGE_URL + movieDetails.posterPath)
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.baseline_image_24)
                .into(imageview)

            Glide
                .with(imageview.context)
                .load(IMAGE_URL + movieDetails.backdropPath)
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.baseline_image_24)
                .into(imageview2)

            saveButton.isVisible = true
            saveButton.setOnClickListener { movieViewModel.saveMovie(movieDetails.toMovie()) }
        }
    }
}