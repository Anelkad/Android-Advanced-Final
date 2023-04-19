package com.example.okhttp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.beust.klaxon.Klaxon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : BaseActivity() {

    var listView: ListView? = null
    val movieListViewModel: MovieListViewModel by viewModels()
    val apiKey = "7754ef3c3751d04070c226b198665358"
    val baseUrl: String = "https://api.themoviedb.org/3/discover/movie?api_key=${apiKey}&with_genres=16,18&language=ru-RU"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)

        // Launch get request
        fetchList(baseUrl)
        observeViewModel()
    }

    private fun observeViewModel(){
        //observeViewModel()
        listView?.isClickable = true
        movieListViewModel.results.observe(this, Observer {


            var arrayAdapter = MovieAdapter(this, it)
                listView?.adapter = arrayAdapter

            listView?.setOnItemClickListener { parent, view, position, id ->
                val id = it[position].id

                val intent = Intent(this, MovieActivity::class.java)
                intent.putExtra("id",id)
                startActivity(intent)
            }
        })
    }

    private fun fetchList(sUrl: String): MovieList? {

        var movieList: MovieList? = null
        showWaitDialog()

        //это надо пересестить в MainActivityView Model через ViewModelScope
        lifecycleScope.launch(Dispatchers.IO) {
            val result = getRequest(sUrl)
            if (result != null) {
                try {
                    // Parse result string JSON to data class
                    movieList = Klaxon().parse<MovieList>(result)

                    withContext(Dispatchers.Main) {
                        // Update view model
                        movieListViewModel.page.value = movieList?.page
                        movieListViewModel.results.value = movieList?.results
                        hideWaitDialog()
                    }
                }
                catch(err:Error) {
                    print("Error when parsing JSON: "+err.localizedMessage)
                }
            }
            else {
                print("Error: Get request returned no response")
            }
        }
        return movieList
    }


}