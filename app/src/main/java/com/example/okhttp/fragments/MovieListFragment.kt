package com.example.okhttp.fragments

import API_KEY
import LANGUAGE
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.okhttp.R
import com.example.okhttp.SavedMovieListViewModel
import com.example.okhttp.adapters.MovieAdapter
import com.example.okhttp.databinding.FragmentMovieListBinding
import com.example.okhttp.models.MovieItem
import com.example.okhttp.utils.Resource
import com.example.okhttp.viewmodels.MovieListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListFragment: Fragment(R.layout.fragment_movie_list) {
    lateinit var binding: FragmentMovieListBinding
    lateinit var movieAdapter: MovieAdapter
    lateinit var movieList: ArrayList<MovieItem>

    var current_page = 1
    var total_pages = 2
    var baseUrl: String = "https://api.themoviedb.org/3/discover/movie?api_key=${API_KEY}&with_genres=16,18&language=${LANGUAGE}&page=${current_page}"

    val movieListViewModel: MovieListViewModel by viewModels()
    val savedMovieListViewModel: SavedMovieListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        current_page = 1
        movieList = ArrayList()
        movieAdapter = MovieAdapter(movieList,savedMovieListViewModel)
        Log.d("onCreate"," done")
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentMovieListBinding.bind(view)
        Log.d("onViewCreated"," done")

        binding.listView.layoutManager = StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
        binding.listView.adapter = movieAdapter

        movieAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putInt("id", it)
            }
            findNavController().navigate(
                R.id.action_movieListFragment_to_movieDetailsFragment,
                bundle
            )
        }

        observeViewModel()

        binding.listView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int){
                super.onScrolled(recyclerView, dx, dy)
                //todo пагинация не работает
                if (!binding.listView.canScrollVertically(1)){
                    if(current_page<=total_pages){
                        current_page++

                        Log.d("onScroll"," done")
                        Toast.makeText(requireContext(),"Downloading...", Toast.LENGTH_SHORT).show()

                        baseUrl = "https://api.themoviedb.org/3/discover/movie?api_key=${API_KEY}&with_genres=16,18&language=${LANGUAGE}&page=${current_page}"
                        movieListViewModel.getMovieList(baseUrl)
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
                    val fetchedMovies = it.getSuccessResult()
                    if (current_page * 20 > movieList.size)
                        movieList.addAll(fetchedMovies.results)
                    movieAdapter.notifyDataSetChanged()
                }
                else -> Unit
            }
        })
    }
}