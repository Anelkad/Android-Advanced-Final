package com.example.okhttp.fragments

import API_KEY
import LANGUAGE
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.okhttp.MovieListViewModel
import com.example.okhttp.R
import com.example.okhttp.adapters.MovieAdapter
import com.example.okhttp.databinding.FragmentMovieListBinding
import com.example.okhttp.models.MovieItem

class MovieListFragment: Fragment(R.layout.fragment_movie_list) {
    lateinit var binding: FragmentMovieListBinding
    lateinit var movieAdapter: MovieAdapter
    lateinit var movieList: ArrayList<MovieItem>

    var current_page = 1
    var total_pages = 2
    var baseUrl: String = "https://api.themoviedb.org/3/discover/movie?api_key=${API_KEY}&with_genres=16,18&language=${LANGUAGE}&page=${current_page}"

    val movieListViewModel: MovieListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        current_page = 1
        movieList = ArrayList()
        movieAdapter = MovieAdapter(movieList)
        Log.d("onCreate"," done")
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentMovieListBinding.bind(view)
        Log.d("onViewCreated"," done")

        binding.listView.adapter = movieAdapter

        observeViewModel()

        binding.listView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int){
                super.onScrolled(recyclerView, dx, dy)
                if (!binding.listView.canScrollVertically(1)){
                    if(current_page<=total_pages){
                        current_page++
                        Log.d("onScroll"," done")
                        baseUrl = "https://api.themoviedb.org/3/discover/movie?api_key=${API_KEY}&with_genres=16,18&language=${LANGUAGE}&page=${current_page}"
                        movieListViewModel.fetchList(baseUrl)
                    }
                }
            }
        })
    }

    private fun observeViewModel(){
        movieListViewModel.results.observe(viewLifecycleOwner, Observer {

            binding.prgBarMovies.isVisible = true

            Log.d("progress","is visible")
            if (current_page * 20 > movieList.size) movieList.addAll(it)
            movieAdapter.notifyDataSetChanged()

            binding.prgBarMovies.isVisible = false

            Log.d("first_observe: ", "${movieList[movieList.size-1].title}: ${movieList.size}")
        })

        movieListViewModel.total_pages.observe(viewLifecycleOwner, Observer{
            total_pages = it
            Log.d("pages_total: ", total_pages.toString())
        })

    }
}