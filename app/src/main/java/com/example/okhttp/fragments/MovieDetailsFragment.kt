package com.example.okhttp.fragments

import IMAGE_URL
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.okhttp.R
import com.example.okhttp.SavedMovieListViewModel
import com.example.okhttp.databinding.FragmentMovieDetailsBinding
import com.example.okhttp.models.Movie
import com.example.okhttp.models.MovieDetails
import com.example.okhttp.utils.Resource
import com.example.okhttp.viewmodels.MovieDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment: Fragment(R.layout.fragment_movie_details)  {

    lateinit var binding: FragmentMovieDetailsBinding

    val movieViewModel: MovieDetailsViewModel by viewModels()
    val args: MovieDetailsFragmentArgs by navArgs()
    val savedMovieListViewModel: SavedMovieListViewModel by viewModels()


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

    private fun saveMovie(movieItem: Movie){
        savedMovieListViewModel.saveMovie(movieItem)
        savedMovieListViewModel.saveMovieState.observe(viewLifecycleOwner, Observer {
            when (it){
                is Resource.Failure -> {
                    hideWaitDialog()
                    Toast.makeText(
                        context, "Cannot save movie!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                is Resource.Loading -> {
                    showWaitDialog()
                }
                is Resource.Success ->{
                    hideWaitDialog()
                    Toast.makeText(
                        context, "Movie \"${it.getSuccessResult().title}\" saved!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> Unit
            }
        })
    }
    //todo don't hardcode
    private fun bindMovie(movieDetails: MovieDetails){
        //todo заменить на binding.with
        binding.textviewTitle.text = movieDetails.title
        binding.textviewDescription.text = movieDetails.overview
        if (movieDetails.tagline.isNotEmpty()) binding.tagline.text = "\"${movieDetails.tagline}\""
        binding.releaseDate.text = "Премьера: ${movieDetails.release_date}"
        binding.runtime.text = "${movieDetails.runtime/60} ч ${movieDetails.runtime%60} мин"
        if (movieDetails.revenue>0) binding.revenue.text = "Кассовые сборы: ${movieDetails.revenue/1000000} млн $"
        Glide
            .with(this)
            .load(IMAGE_URL+movieDetails.poster_path)
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.baseline_image_24)
            .into(binding.imageview)
        Glide
            .with(this)
            .load(IMAGE_URL+movieDetails.backdrop_path)
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.baseline_image_24)
            .into(binding.imageview2)
        binding.saveButton.isVisible = true
        binding.saveButton.setOnClickListener {
            saveMovie(movieDetails.toMovie())
        }
    }

    private lateinit var waitDialog: Dialog
    private fun showWaitDialog(){
        if (!this::waitDialog.isInitialized) {
            waitDialog = Dialog(requireActivity())
            waitDialog.setContentView(R.layout.wait_dialog)

            waitDialog.setCancelable(false)
            waitDialog.setCanceledOnTouchOutside(false)
        }
        if (!waitDialog.isShowing) waitDialog.show()
    }

    private fun hideWaitDialog(){
        if (this::waitDialog.isInitialized or waitDialog.isShowing) waitDialog.dismiss()
    }

}