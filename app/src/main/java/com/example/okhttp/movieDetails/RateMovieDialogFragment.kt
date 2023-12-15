package com.example.okhttp.movieDetails

import com.example.core.utils.IntentConstants.MOVIE_ID
import com.example.core.utils.IntentConstants.RATING
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.okhttp.R
import com.example.okhttp.databinding.LayoutBottomSheetRateMovieBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RateMovieDialogFragment(
    private val saveRating: (Double?) -> Unit = {}
) : BottomSheetDialogFragment() {

    private val viewModel: RateMovieViewModel by viewModels()
    private var binding: LayoutBottomSheetRateMovieBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutBottomSheetRateMovieBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.state.onEach { state ->
            when (state) {
                is RateMovieViewModel.State.Error -> {
                    Toast.makeText(
                        context,
                        state.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is RateMovieViewModel.State.HideLoading -> {
                    binding?.apply {
                        progressButton.visibility = View.GONE
                        tvBtnSave.text = getString(R.string.rate_movie)
                    }
                }

                is RateMovieViewModel.State.ShowLoading -> {
                    binding?.apply {
                        tvBtnSave.text = ""
                        progressButton.visibility = View.VISIBLE
                    }
                }

                is RateMovieViewModel.State.RatedMovie -> {
                    saveRating(binding?.rating?.rating?.toDouble())
                    dismiss()
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun bindViews() {
        arguments?.getDouble(RATING)?.toFloat()?.let {
            binding?.rating?.rating = it
        }
        binding?.apply {
            btnSave.visibility = View.VISIBLE
        }
    }

    private fun setupListeners() {
        val movieId = arguments?.getInt(MOVIE_ID)
        binding?.btnSave?.setOnClickListener {
            val rating = binding?.rating?.rating?.toDouble()
            if (rating != null && movieId != null) {
                viewModel.rateMovie(movieId = movieId, rating = rating)
            }
        }
        binding?.btnClose?.setOnClickListener {
            dismiss()
        }
    }
}