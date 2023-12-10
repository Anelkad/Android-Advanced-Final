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
import com.example.okhttp.domain.model.MovieDetails
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

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
        movieViewModel.getIsMovieSaved(args.id)
        bindViews()
        setupObservers()
        registerWaitDialogDelegate(this)
    }

    private fun bindViews() {
        binding?.btnBack?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupObservers() {
        movieViewModel.state.onEach { state ->
            when (state) {
                is MovieDetailsViewModel.State.Error -> {
                    Toast.makeText(
                        context,
                        getString(R.string.smth_went_wrong),
                        Toast.LENGTH_SHORT
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

                is MovieDetailsViewModel.State.IsMovieSaved -> {

                }
            }
        }.launchIn(lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {
            movieViewModel.effect.collect {
                when (it) {
                    is MovieDetailsViewModel.Effect.ShowToast -> {
                        Toast.makeText(
                            context,
                            it.text,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    MovieDetailsViewModel.Effect.ShowWaitDialog -> {
                        showWaitDialog()
                    }

                    MovieDetailsViewModel.Effect.HideWaitDialog -> {
                        hideWaitDialog()
                    }

                    MovieDetailsViewModel.Effect.MovieSaved -> {
                        Toast.makeText(
                            context,
                            getString(R.string.movie_saved_title),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun bindMovie(movieDetails: MovieDetails) {
        binding?.apply {
            tvTitle.text = movieDetails.title
            tvDescription.text = movieDetails.overview
            if (movieDetails.tagline.isNotEmpty()) tvTagline.text =
                getString(R.string.tagline, movieDetails.tagline)
            tvReleaseDate.text = getString(R.string.premiere, movieDetails.releaseDate)
            tvRuntime.text = getString(
                R.string.runtime,
                movieDetails.runtime / 60,
                movieDetails.runtime % 60
            )
            if (movieDetails.revenue > 0) tvRevenue.text =
                getString(R.string.revenue, movieDetails.revenue / 1000000)

            Glide
                .with(ivPoster.context)
                .load(IMAGE_URL + movieDetails.posterPath)
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.baseline_image_24)
                .into(ivPoster)

            Glide
                .with(ivBackgroundPoster.context)
                .load(IMAGE_URL + movieDetails.backdropPath)
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.baseline_image_24)
                .into(ivBackgroundPoster)

            btnSave.isVisible = true
            btnSave.setOnClickListener { movieViewModel.saveMovie(movieDetails.id) }
        }
    }
}