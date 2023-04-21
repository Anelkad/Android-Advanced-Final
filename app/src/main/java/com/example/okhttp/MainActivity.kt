package com.example.okhttp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.okhttp.adapters.MovieAdapter

class MainActivity : BaseActivity() {

    var listView: ListView? = null
    val movieListViewModel: MovieListViewModel by viewModels()
    val apiKey = "7754ef3c3751d04070c226b198665358"
    val language = "ru-RU"
    val baseUrl: String = "https://api.themoviedb.org/3/discover/movie?api_key=${apiKey}&with_genres=16,18&language=${language}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)

        // Launch get request
        movieListViewModel.fetchList(baseUrl)
        observeViewModel()
    }

    private fun observeViewModel(){
        showWaitDialog()
        //observeViewModel()
        listView?.isClickable = true
        movieListViewModel.results.observe(this, Observer {

            var arrayAdapter = MovieAdapter(this, it)
                listView?.adapter = arrayAdapter

            hideWaitDialog()

            listView?.setOnItemClickListener { parent, view, position, id ->
                val id = it[position].id

                val intent = Intent(this, MovieActivity::class.java)
                intent.putExtra("id",id)
                startActivity(intent)
            }
        })
    }

}