package com.example.okhttp

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.okhttp.adapters.MovieAdapter
import com.example.okhttp.databinding.ActivityMainBinding
import com.example.okhttp.models.MovieItem

class MainActivity : BaseActivity() {

    val apiKey = "7754ef3c3751d04070c226b198665358"
    val language = "ru-RU"
    var current_page = 1
    var total_pages = 2
    var baseUrl: String = "https://api.themoviedb.org/3/discover/movie?api_key=${apiKey}&with_genres=16,18&language=${language}&page=${current_page}"

    lateinit var movieList: ArrayList<MovieItem>

    lateinit var binding: ActivityMainBinding
    lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieList = ArrayList()
        movieAdapter = MovieAdapter(this, movieList)
        binding.listView.adapter = movieAdapter


        binding.apply {
            firstObserveViewModel()
        }

        binding.listView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int){
                super.onScrolled(recyclerView, dx, dy)
                 if (!binding.listView.canScrollVertically(1)){
                     if(current_page<=total_pages){
                         current_page++
                         observeViewModel()
                     }
                 }
            }
        })
        Log.d("onCreate: ", movieList.size.toString())
    }

    private fun observeViewModel() {
        val movieListViewModel2: MovieListViewModel by viewModels()

        baseUrl = "https://api.themoviedb.org/3/discover/movie?api_key=${apiKey}&with_genres=16,18&language=${language}&page=${current_page}"

        movieListViewModel2.fetchList(baseUrl)

        var new_page: ArrayList<MovieItem>? = null
        movieListViewModel2.results.observe(this, Observer {
            new_page = it
        Log.d("after_observe: ", movieList.size.toString())
        })

        movieList.addAll(new_page!!)
        movieAdapter.notifyDataSetChanged()


    }

    private fun firstObserveViewModel(){

        val movieListViewModel1: MovieListViewModel by viewModels()

        showWaitDialog()

        movieListViewModel1.fetchList(baseUrl)

        movieListViewModel1.results.observe(this, Observer {
            movieList.addAll(it)
            movieAdapter.notifyDataSetChanged()
            hideWaitDialog()
            Log.d("first_observe: ", movieList.size.toString())
        })

        movieListViewModel1.total_pages.observe(this, Observer{
            total_pages = it
            Log.d("pages_total: ", total_pages.toString())
        })

    }


}