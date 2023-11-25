package com.example.okhttp.savedMovieList

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
class SavedMovieFragment : Fragment() {

    lateinit var movieList: ArrayList<Movie>
    lateinit var binding: FragmentSavedMovieBinding
    lateinit var movieAdapter: SavedMovieAdapter
    private lateinit var waitDialog: Dialog

    val savedMovieListViewModel: SavedMovieListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        movieList = ArrayList()
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

        binding = FragmentSavedMovieBinding.inflate(inflater, container, false)
        binding.listView.adapter = movieAdapter

        setupObservers()
        return binding.root
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
                    binding.progressBar.isVisible = false
                }

                is SavedMovieListViewModel.State.ShowLoading -> {
                    binding.progressBar.isVisible = true
                }

                is SavedMovieListViewModel.State.SavedMovieList -> {
                    binding.progressBar.isVisible = false //todo Hloading
                    movieList.clear()
                    movieList.addAll(state.movies)
                    movieAdapter.submitList(movieList.toMutableList())
                    binding.noSavedMovie.isVisible = movieList.isEmpty()

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
        if (!this::waitDialog.isInitialized) {
            waitDialog = Dialog(requireActivity())
            waitDialog.setContentView(R.layout.wait_dialog)
            waitDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            waitDialog.setCancelable(false)
            waitDialog.setCanceledOnTouchOutside(false)
        }
        if (!waitDialog.isShowing) waitDialog.show()
    }

    private fun hideWaitDialog() {
        if (this::waitDialog.isInitialized or waitDialog.isShowing) waitDialog.dismiss()
    }
}