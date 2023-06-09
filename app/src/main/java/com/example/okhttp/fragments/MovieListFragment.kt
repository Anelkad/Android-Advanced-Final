package com.example.okhttp.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.okhttp.R
import com.example.okhttp.SavedMovieListViewModel
import com.example.okhttp.adapters.MovieAdapter
import com.example.okhttp.adapters.MovieLoadStateAdapter
import com.example.okhttp.adapters.PagedMovieAdapter
import com.example.okhttp.databinding.FragmentMovieListBinding
import com.example.okhttp.models.Movie
import com.example.okhttp.utils.Resource
import com.example.okhttp.viewmodels.MovieListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieListFragment: Fragment(R.layout.fragment_movie_list) {
    lateinit var binding: FragmentMovieListBinding
    lateinit var movieAdapter: PagedMovieAdapter
    lateinit var movieList: ArrayList<Movie>

    var current_page = 1
    var total_pages = 2

    val movieListViewModel: MovieListViewModel by viewModels()
    val savedMovieListViewModel: SavedMovieListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        current_page = 1
        movieList = ArrayList()
        movieAdapter = PagedMovieAdapter()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentMovieListBinding.bind(view)

        binding.listView.layoutManager = StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
        binding.listView.adapter = movieAdapter.withLoadStateFooter(
            MovieLoadStateAdapter { movieAdapter.retry()}
        )

        lifecycleScope.launch {
            movieListViewModel.pagedMovieList.collectLatest {
                movieAdapter.submitData(it)
            }
        }

        movieAdapter.setOnMovieClickListener {
            val bundle = Bundle().apply {
                putInt("id", it)
            }
            findNavController().navigate(
                R.id.action_movieListFragment_to_movieDetailsFragment,
                bundle
            )
        }

        movieAdapter.setSaveMovieClickListener {saveMovie(it)}

        binding.btnRetry.setOnClickListener {
            //todo как проверить retry кнопку?
            movieAdapter.retry()
        }

        movieAdapter.addLoadStateListener { loadState ->

            if (loadState.refresh is LoadState.Loading) {
                binding.btnRetry.isVisible = false
                binding.progressBar.isVisible = true
            }else{
                binding.progressBar.isVisible = false

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

        //observeViewModel()
        //oldOnScroll()

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

    private fun oldOnScroll(){
        binding.listView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int){
                super.onScrolled(recyclerView, dx, dy)
                if (!binding.listView.canScrollVertically(1)){
                    if (current_page<=total_pages) {
                        current_page++
                        movieListViewModel.getMovieList(current_page)
                    }
                }
            }
        })
    }

    private fun observeViewModel(){
        movieListViewModel.movieListState.observe(viewLifecycleOwner, Observer {
            when (it){
                is Resource.Failure -> {
                    binding.progressBar.isVisible = false
                }
                is Resource.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is Resource.Success ->{
                    binding.progressBar.isVisible = false
                    val fetchedMovies = it.getSuccessResult().results
                    total_pages = it.getSuccessResult().total_pages
                    if (current_page * 20 > movieList.size){
                        movieList.addAll(fetchedMovies)
                        //movieAdapter.submitList(movieList.toMutableList())
                    }
                }
                else -> Unit
            }
        })
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