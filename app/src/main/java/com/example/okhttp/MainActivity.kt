package com.example.okhttp

import API_KEY
import LANGUAGE
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.okhttp.adapters.MovieAdapter
import com.example.okhttp.databinding.ActivityMainBinding
import com.example.okhttp.models.MovieItem

class MainActivity : AppCompatActivity() {

    var current_page = 1
    var total_pages = 2
    var baseUrl: String = "https://api.themoviedb.org/3/discover/movie?api_key=${API_KEY}&with_genres=16,18&language=${LANGUAGE}&page=${current_page}"

    lateinit var movieList: ArrayList<MovieItem>
    lateinit var binding: ActivityMainBinding
    lateinit var movieAdapter: MovieAdapter
    val movieListViewModel: MovieListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieList = ArrayList()
        movieAdapter = MovieAdapter(this, movieList)
        binding.listView.adapter = movieAdapter

        firstObserveViewModel()

        binding.listView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int){
                super.onScrolled(recyclerView, dx, dy)
                 if (!binding.listView.canScrollVertically(1)){
                     if(current_page<=total_pages){
                         current_page++
                         baseUrl = "https://api.themoviedb.org/3/discover/movie?api_key=${API_KEY}&with_genres=16,18&language=${LANGUAGE}&page=${current_page}"
                         movieListViewModel.fetchList(baseUrl)
                     }
                 }
            }
        })
    }

    private fun firstObserveViewModel(){

        movieListViewModel.fetchList(baseUrl)

        movieListViewModel.results.observe(this, Observer {

            binding.prgBarMovies.isVisible = true

            Log.d("progress","is visible")
            movieList.addAll(it)
            movieAdapter.notifyDataSetChanged()

            binding.prgBarMovies.isVisible = false

            Log.d("first_observe: ", "${movieList[movieList.size-1].title}: ${movieList.size}")
        })

        movieListViewModel.total_pages.observe(this, Observer{
            total_pages = it
            Log.d("pages_total: ", total_pages.toString())
        })

    }


}