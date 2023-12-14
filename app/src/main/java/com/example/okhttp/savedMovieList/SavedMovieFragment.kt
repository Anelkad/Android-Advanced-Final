package com.example.okhttp.savedMovieList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
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
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SavedMovieFragment : Fragment(),
    DialogDelegate by WaitDialogDelegate() {

    private var binding: FragmentSavedMovieBinding? = null
    private var movieAdapter: SavedMovieAdapter? = null
    private val savedMovieListViewModel: SavedMovieListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerWaitDialogDelegate(this)
        bindViews()
        setupObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedMovieBinding.inflate(inflater, container, false)
        return binding?.root
    }

    private fun bindViews() {
        binding?.tvNoSavedMovie?.isVisible = movieAdapter?.currentList?.isEmpty() == true
        //todo add crashlytics
        movieAdapter = SavedMovieAdapter(
            onItemClickListener = ::navigateToDetails,
            deleteMovieListener = { savedMovieListViewModel.deleteMovie(it) }
        )
        binding?.rvMovies?.adapter = movieAdapter
        binding?.swipeRefresh?.setOnRefreshListener {
            savedMovieListViewModel.getMovieList()
        }
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
                    binding?.swipeRefresh?.isRefreshing = false
                }

                is SavedMovieListViewModel.State.ShowLoading -> {
                    binding?.apply {
                        if (!swipeRefresh.isRefreshing) progressBar.isVisible = true
                    }
                }

                is SavedMovieListViewModel.State.SavedMovieList -> {
                    movieAdapter?.submitList(state.movies)
                    binding?.tvNoSavedMovie?.isVisible = state.movies.isEmpty()
                }

                else -> Unit
            }
        }.launchIn(lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {
            savedMovieListViewModel.effect.collect { effect ->
                when (effect) {
                    is SavedMovieListViewModel.Effect.ShowToast -> {
                        Toast.makeText(
                            context,
                            effect.text,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    SavedMovieListViewModel.Effect.ShowWaitDialog -> {
                        showWaitDialog()
                    }

                    SavedMovieListViewModel.Effect.HideWaitDialog -> {
                        hideWaitDialog()
                    }

                    is SavedMovieListViewModel.Effect.MovieDeleted -> {
                        Toast.makeText(
                            context,
                            getString(R.string.movie_deleted_title),
                            Toast.LENGTH_SHORT
                        ).show()
                        movieAdapter?.deleteMovie(
                            movieId = effect.movieId,
                            update = {
                                binding?.tvNoSavedMovie?.isVisible =
                                    movieAdapter?.currentList?.isEmpty() == true
                            }
                        )
                    }
                }
            }
        }
    }

    private fun navigateToDetails(movieId: Int) {
        val bundle = bundleOf("id" to movieId)
        findNavController().navigate(
            R.id.action_savedMovieFragment_to_movieDetailsFragment,
            bundle
        )
    }
}