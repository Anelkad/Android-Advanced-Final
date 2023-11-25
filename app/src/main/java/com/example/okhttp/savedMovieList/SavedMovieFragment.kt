package com.example.okhttp.savedMovieList

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
import com.example.okhttp.R
import com.example.okhttp.databinding.FragmentSavedMovieBinding
import com.example.okhttp.models.Movie
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SavedMovieFragment : Fragment(R.layout.fragment_saved_movie) {

    private var movieList: ArrayList<Movie> = arrayListOf()
    private var binding: FragmentSavedMovieBinding? = null
    private var movieAdapter: SavedMovieAdapter? = null
    private var waitDialog: Dialog? = null
    private val savedMovieListViewModel: SavedMovieListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSavedMovieBinding.bind(view)
        bindViews()
        setupObservers()
    }

    private fun bindViews() {
        movieAdapter = SavedMovieAdapter(
            onItemClickListener = {
                val bundle = Bundle().apply {
                    putInt("id", it)
                }
                findNavController().navigate(
                    R.id.action_savedMovieFragment_to_movieDetailsFragment,
                    bundle
                )
            },
            deleteMovieListener = { deleteMovie(it) }
        )
        savedMovieListViewModel.getMovieList()
        binding?.listView?.adapter = movieAdapter
    }

    private fun setupObservers() {
        savedMovieListViewModel.state.onEach { state ->
            when (state) {
                is SavedMovieListViewModel.State.Error -> {
                    Toast.makeText(
                        context, "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }

                is SavedMovieListViewModel.State.HideLoading -> {
                    binding?.progressBar?.isVisible = false
                }

                is SavedMovieListViewModel.State.ShowLoading -> {
                    binding?.progressBar?.isVisible = true
                }

                is SavedMovieListViewModel.State.SavedMovieList -> {
                    binding?.progressBar?.isVisible = false //todo Hloading
                    movieList.clear()
                    movieList.addAll(state.movies)
                    movieAdapter?.submitList(movieList.toMutableList())
                    binding?.noSavedMovie?.isVisible = movieList.isEmpty()

                }

                is SavedMovieListViewModel.State.MovieDeleted -> {
                    Toast.makeText(
                        context, "Movie deleted!",
                        Toast.LENGTH_LONG
                    ).show()
                }

                is SavedMovieListViewModel.State.ShowWaitDialog -> {
                    showWaitDialog()
                }

                is SavedMovieListViewModel.State.HideWaitDialog -> {
                    hideWaitDialog()
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun deleteMovie(movieId: Int) {
        savedMovieListViewModel.deleteMovie(movieId)
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