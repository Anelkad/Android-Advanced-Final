package com.example.okhttp.savedMovieList

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
import com.example.okhttp.delegates.DialogDelegate
import com.example.okhttp.delegates.WaitDialogDelegate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SavedMovieFragment : Fragment(R.layout.fragment_saved_movie),
    DialogDelegate by WaitDialogDelegate() {

    private var binding: FragmentSavedMovieBinding? = null
    private var movieAdapter: SavedMovieAdapter? = null
    private val savedMovieListViewModel: SavedMovieListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSavedMovieBinding.bind(view)
        registerWaitDialogDelegate(this)
        savedMovieListViewModel.getMovieList()
        movieAdapter?.currentList
        bindViews()
        setupObservers()
    }

    private fun bindViews() {
        movieAdapter = SavedMovieAdapter(
            onItemClickListener = ::navigateToDetails,
            deleteMovieListener = { savedMovieListViewModel.deleteMovie(it) }
        )
        binding?.listView?.adapter = movieAdapter
    }

    private fun setupObservers() {
        savedMovieListViewModel.state.onEach { state ->
            when (state) {
                is SavedMovieListViewModel.State.Error -> {
                    Toast.makeText(
                        context,
                        getString(R.string.smth_went_wrong),
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
                    movieAdapter?.submitList(state.movies)
                    binding?.noSavedMovie?.isVisible = movieAdapter?.currentList?.isEmpty() == true
                }

                is SavedMovieListViewModel.State.MovieDeleted -> {
                    Toast.makeText(
                        context,
                        getString(R.string.movie_deleted_title),
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

    private fun navigateToDetails(movieId: Int) {
        val bundle = Bundle().apply {
            putInt("id", movieId)
        }
        findNavController().navigate(
            R.id.action_savedMovieFragment_to_movieDetailsFragment,
            bundle
        )
    }
}