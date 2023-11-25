package com.example.okhttp.movieDetails

import IMAGE_URL
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.example.okhttp.models.Movie
import com.example.okhttp.models.MovieDetails
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private var binding: FragmentMovieDetailsBinding? = null
    private val movieViewModel: MovieDetailsViewModel by viewModels()
    private val args: MovieDetailsFragmentArgs by navArgs()
    private var waitDialog: Dialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieDetailsBinding.bind(view)
        binding?.backButton?.setOnClickListener {
            findNavController().popBackStack()
        }
        setupObservers()
    }

    private fun setupObservers() {
        movieViewModel.state.onEach { state ->
            when (state) {
                is MovieDetailsViewModel.State.Error -> {
                    Toast.makeText(
                        context, "Something went wrong",
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
                        context, "Movie \"${state.movie.title}\" saved!",
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieViewModel.getMovie(args.id)
    }

    private fun saveMovie(movieItem: Movie) = movieViewModel.saveMovie(movieItem)


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
            saveButton.setOnClickListener { saveMovie(movieDetails.toMovie()) }
        }
    }

    private fun showWaitDialog() {
        if (waitDialog == null) {
            waitDialog = Dialog(requireActivity()).apply {
                setContentView(R.layout.wait_dialog)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setCancelable(false)
                setCanceledOnTouchOutside(false)
            }
        }
        if (waitDialog?.isShowing == false) waitDialog?.show()
    }

    private fun hideWaitDialog() {
        if (waitDialog != null || waitDialog?.isShowing == true) waitDialog?.dismiss()
    }
}