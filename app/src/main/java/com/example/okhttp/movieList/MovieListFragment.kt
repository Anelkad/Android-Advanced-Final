package com.example.okhttp.movieList

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
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.okhttp.R
import com.example.okhttp.databinding.FragmentMovieListBinding
import com.example.okhttp.delegates.DialogDelegate
import com.example.okhttp.delegates.WaitDialogDelegate
import com.example.okhttp.firebase.EventManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieListFragment : Fragment(),
    DialogDelegate by WaitDialogDelegate() {

    private var binding: FragmentMovieListBinding? = null
    private var movieAdapter: PagedMovieAdapter? = null
    private val movieListViewModel: MovieListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerWaitDialogDelegate(this)
        setupObserver()
        bindViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    private fun bindViews() {
        movieAdapter = PagedMovieAdapter(
            onMovieClickListener = ::navigateToDetails,
            saveMovieListener = { movieListViewModel.saveMovie(it) }
        )
        binding?.apply {
            val layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL).apply {
                gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
            }
            rvMovies.layoutManager = layoutManager
            rvMovies.adapter = movieAdapter?.withLoadStateFooter(
                MovieLoadStateAdapter { movieAdapter?.retry() }
            )
            rvMovies.itemAnimator = null
            swipeRefresh.setOnRefreshListener {
                movieAdapter?.refresh()
            }
            btnRetry.setOnClickListener {
                movieAdapter?.retry()
            }
            progressBar.isVisible = true
        }
        lifecycleScope.launch {
            movieListViewModel.pagedMovieList.collectLatest {
                movieAdapter?.submitData(it)
            }
        }
        movieAdapter?.addLoadStateListener { loadStateListener(it) }
    }

    private fun loadStateListener(loadState: CombinedLoadStates) {
        if (loadState.refresh is LoadState.Loading) {
            binding?.btnRetry?.isVisible = false
        } else {
            binding?.progressBar?.isVisible = false
            if (binding?.swipeRefresh?.isRefreshing == true) {
                binding?.swipeRefresh?.isRefreshing = false
            }
            val errorState = when {
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                loadState.refresh is LoadState.Error -> {
                    binding?.btnRetry?.isVisible = true
                    loadState.refresh as LoadState.Error
                }
                else -> null
            }
            errorState?.let {
                Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun navigateToDetails(movieId: Int) {
        val bundle = bundleOf(
            "id" to movieId
        )
        findNavController().navigate(
            R.id.action_movieListFragment_to_movieDetailsFragment,
            bundle
        )
    }

    private fun setupObserver() {
        movieListViewModel.state.onEach { state ->
            when (state) {
                is MovieListViewModel.State.Error -> {
                    Toast.makeText(
                        context,
                        getString(R.string.smth_went_wrong),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is MovieListViewModel.State.HideLoading -> {
                    //binding.progressBar.isVisible = false
                }

                is MovieListViewModel.State.ShowLoading -> {
                    //binding.progressBar.isVisible = true
                }
            }
        }.launchIn(lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {
            movieListViewModel.effect.collect {
                when (it) {
                    is MovieListViewModel.Effect.ShowToast -> {
                        Toast.makeText(
                            context,
                            it.text,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    MovieListViewModel.Effect.ShowWaitDialog -> {
                        showWaitDialog()
                    }
                    MovieListViewModel.Effect.HideWaitDialog -> {
                        hideWaitDialog()
                    }
                    is MovieListViewModel.Effect.MovieSaved -> {
                        EventManager.logEvent(
                            eventName = "movie_saved",
                            bundle = bundleOf("movieId" to it.movieId)
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
}