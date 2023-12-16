package com.example.okhttp.movieDetails

import com.example.core.utils.ApiConstants.IMAGE_URL
import com.example.core.utils.IntentConstants.MOVIE_ID
import com.example.core.utils.IntentConstants.RATING
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
        movieViewModel.getMovieDetails(args.id)
        bindViews()
        setupObservers()
        registerWaitDialogDelegate(this)
    }

    private fun bindViews() {
        binding?.apply {
            llMovieDetails.visibility = View.GONE
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            srl.setOnRefreshListener {
                movieViewModel.getMovieDetails(args.id)
            }
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
                    binding?.srl?.isRefreshing = false
                }

                is MovieDetailsViewModel.State.ShowLoading -> {
                    binding?.progressBar?.isVisible = true
                }

                is MovieDetailsViewModel.State.ShowMovieDetails -> {
                    bindMovie(state.movie)
                }

                is MovieDetailsViewModel.State.IsMovieSaved -> {
                    binding?.btnSave?.apply {
                        if (state.details.favorite) {
                            setImageResource(R.drawable.baseline_favorite_24)
                            setOnClickListener { movieViewModel.deleteMovie(state.details.id) }
                        } else {
                            setImageResource(R.drawable.baseline_favorite_border_24)
                            setOnClickListener { movieViewModel.saveMovie(state.details.id) }
                        }
                    }
                    updateRating(state.details.rated?.value)
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
                        movieViewModel.getIsMovieSaved(args.id)
                    }

                    MovieDetailsViewModel.Effect.MovieDeleted -> {
                        Toast.makeText(
                            context,
                            getString(R.string.movie_deleted_title),
                            Toast.LENGTH_SHORT
                        ).show()
                        movieViewModel.getIsMovieSaved(args.id)
                    }
                }
            }
        }
    }

    private fun showRateMovieDialog(rating: Double? = 0.0) {
        if (rating == null) return
        val dialog = RateMovieDialogFragment(::updateRating)
        val bundle = Bundle()
        bundle.putInt(MOVIE_ID, args.id)
        bundle.putDouble(RATING, rating)
        dialog.arguments = bundle
        dialog.show(childFragmentManager, dialog.tag)
    }

    private fun updateRating(rating: Double?) {
        binding?.apply {
            if (rating == 0.0 || rating == null) {
                btnRate.setImageResource(R.drawable.baseline_star_border_24)
                tvRate.text = ""
                btnRate.setOnClickListener {
                    showRateMovieDialog(0.0)
                }
            } else {
                btnRate.setOnClickListener {
                    showRateMovieDialog(rating)
                }
                tvRate.text = rating.toString()
                btnRate.setImageResource(R.drawable.baseline_star_rate_24)
            }
        }
    }

    private fun bindMovie(movieDetails: MovieDetails) {
        binding?.apply {
            llMovieDetails.visibility = View.VISIBLE
            tvTitle.text = movieDetails.title
            tvDescription.text = movieDetails.overview
            tvRating.text = getString(R.string.rating, movieDetails.voteAverage)
            if (movieDetails.tagline.isNotEmpty()) {
                tvTagline.visibility = View.VISIBLE
                tvTagline.text = getString(R.string.tagline, movieDetails.tagline)
            }
            tvReleaseDate.text = getString(R.string.premiere, movieDetails.releaseDate)
            tvRuntime.text = getString(
                R.string.runtime,
                movieDetails.runtime / 60,
                movieDetails.runtime % 60
            )
            if (movieDetails.revenue > 0) {
                tvRevenue.visibility = View.VISIBLE
                tvRevenue.text = getString(R.string.revenue, movieDetails.revenue / 1000000)
            }
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
        }
    }
}