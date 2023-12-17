package com.example.okhttp.search

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
import com.example.okhttp.databinding.FragmentSearchMovieBinding
import com.example.okhttp.delegates.DialogDelegate
import com.example.okhttp.delegates.WaitDialogDelegate
import com.example.okhttp.firebase.EventManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchMovieFragment : Fragment(),
    DialogDelegate by WaitDialogDelegate() {

    private var binding: FragmentSearchMovieBinding? = null
    private var movieAdapter: SearchMovieAdapter? = null
    private val savedMovieListViewModel: SearchMovieListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerWaitDialogDelegate(this)
        bindViews()
        setupObservers()
        if (savedInstanceState == null) {
            savedMovieListViewModel.loadMovies(binding?.etSearch?.text.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchMovieBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("saveQuery", binding?.etSearch?.text.toString())
    }

    private fun bindViews() {
        movieAdapter = SearchMovieAdapter(
            onItemClickListener = ::navigateToDetails,
            saveMovieListener = { savedMovieListViewModel.saveMovie(it) }
        )
        binding?.apply {
            etSearch.setText(arguments?.getString("query"))
            rvMovies.adapter = movieAdapter
            swipeRefresh.setOnRefreshListener {
                savedMovieListViewModel.search(etSearch.text.toString())
            }
            ilSearch.setEndIconOnClickListener {
                savedMovieListViewModel.search(etSearch.text.toString())
            }
        }
    }

    private fun setupObservers() {
        savedMovieListViewModel.state.onEach { state ->
            when (state) {
                is SearchMovieListViewModel.State.Error -> {
                    Toast.makeText(
                        context,
                        getString(R.string.smth_went_wrong),
                        Toast.LENGTH_LONG
                    ).show()
                }

                is SearchMovieListViewModel.State.HideLoading -> {
                    binding?.progressBar?.isVisible = false
                    binding?.swipeRefresh?.isRefreshing = false
                }

                is SearchMovieListViewModel.State.ShowLoading -> {
                    movieAdapter?.submitList(emptyList())
                    binding?.apply {
                        if (!swipeRefresh.isRefreshing) progressBar.isVisible = true
                        tvNoFoundMovie.visibility = View.GONE
                    }
                }

                is SearchMovieListViewModel.State.FoundMovieList -> {
                    movieAdapter?.submitList(state.movies)
                    binding?.tvNoFoundMovie?.isVisible = state.movies.isEmpty()
                }
            }
        }.launchIn(lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {
            savedMovieListViewModel.effect.collect { effect ->
                when (effect) {
                    is SearchMovieListViewModel.Effect.ShowToast -> {
                        Toast.makeText(
                            context,
                            effect.text,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    SearchMovieListViewModel.Effect.ShowWaitDialog -> {
                        showWaitDialog()
                    }

                    SearchMovieListViewModel.Effect.HideWaitDialog -> {
                        hideWaitDialog()
                    }

                    is SearchMovieListViewModel.Effect.MovieSaved -> {
                        EventManager.logEvent(
                            eventName = "movieSaved",
                            bundle = bundleOf("movieId" to effect.movieId)
                        )
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

    private fun navigateToDetails(movieId: Int) {
        val bundle = bundleOf("id" to movieId)
        findNavController().navigate(
            R.id.action_searchMovieFragment_to_movieDetailsFragment,
            bundle
        )
    }
}