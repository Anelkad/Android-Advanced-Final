package com.example.okhttp.movieList

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
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.okhttp.R
import com.example.okhttp.databinding.FragmentMovieListBinding
import com.example.okhttp.models.Movie
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieListFragment: Fragment(R.layout.fragment_movie_list) {
    lateinit var binding: FragmentMovieListBinding
    lateinit var movieAdapter: PagedMovieAdapter

    private val movieListViewModel: MovieListViewModel by viewModels()

    private lateinit var waitDialog: Dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentMovieListBinding.bind(view)

        movieAdapter = PagedMovieAdapter(
            {
                val bundle = Bundle().apply {
                    putInt("id", it)
                }
                findNavController().navigate(
                    R.id.action_movieListFragment_to_movieDetailsFragment,
                    bundle
                )
            },
            {
                saveMovie(it)
            }
        )

        setupObserver()

        binding.listView.layoutManager = StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
        binding.listView.adapter = movieAdapter.withLoadStateFooter(
            MovieLoadStateAdapter { movieAdapter.retry()}
        )

        binding.progressBar.isVisible = true

        lifecycleScope.launch {
            movieListViewModel.pagedMovieList.collectLatest {
                movieAdapter.submitData(it)
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            movieAdapter.refresh()
        }

        binding.btnRetry.setOnClickListener {
            movieAdapter.retry()
        }

        movieAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                binding.btnRetry.isVisible = false
            }
            else {
                binding.progressBar.isVisible = false
                if (binding.swipeRefresh.isRefreshing) {
                    binding.swipeRefresh.isRefreshing = false;
                }
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> {
                        binding.btnRetry.isVisible = true
                        loadState.refresh as LoadState.Error
                    }
                    else -> null
                }
                errorState?.let {
                    Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupObserver() {
        movieListViewModel.state.onEach { state ->
            when (state) {
                is MovieListViewModel.State.Error -> {
                    Toast.makeText(
                        context, "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }

                is MovieListViewModel.State.HideLoading -> {
                    //binding.progressBar.isVisible = false
                }

                is MovieListViewModel.State.ShowLoading -> {
                    //binding.progressBar.isVisible = true
                }

                is MovieListViewModel.State.MovieSaved -> {
                    Toast.makeText(
                        context, "Movie \"${state.movie.title}\" saved!",
                        Toast.LENGTH_LONG
                    ).show()
                }

                is MovieListViewModel.State.ShowWaitDialog -> {
                    showWaitDialog()
                }

                is MovieListViewModel.State.HideWaitDialog -> {
                    hideWaitDialog()
                }
            }
        }.launchIn(lifecycleScope)
    }

    private val saveMovie: (Movie) -> (Unit) = { movie ->
        movieListViewModel.saveMovie(movie)
    }
    private fun showWaitDialog(){
        if (!this::waitDialog.isInitialized) {
            waitDialog = Dialog(requireActivity())
            waitDialog.setContentView(R.layout.wait_dialog)
            waitDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            waitDialog.setCancelable(false)
            waitDialog.setCanceledOnTouchOutside(false)
        }
        if (!waitDialog.isShowing) waitDialog.show()
    }

    private fun hideWaitDialog(){
        if (this::waitDialog.isInitialized or waitDialog.isShowing) waitDialog.dismiss()
    }
}