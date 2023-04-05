package com.example.okhttp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.beust.klaxon.Klaxon
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieActivity : BaseActivity() {

    var id: Int = 0
    val movieViewModel: MovieViewModel by viewModels()
    val imageUrl = "https://image.tmdb.org/t/p/w500/"

    var textViewTitle: TextView? = null
    var textViewDescription: TextView? = null
    var imageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        imageView = findViewById(R.id.imageview)
        textViewTitle = findViewById(R.id.textview_title)
        textViewDescription = findViewById(R.id.textview_description)

        id = intent.getIntExtra("id", 0)
        val baseUrl: String = "https://api.themoviedb.org/3/movie/${id}?api_key=7754ef3c3751d04070c226b198665358&language=en-US"

        fetchMovie(baseUrl)
        observeViewModel()

    }

    private fun observeViewModel(){
        //observeViewModel()
        movieViewModel.original_title.observe(this, Observer {
            textViewTitle?.text = it
        })

        movieViewModel.overview.observe(this, Observer {
            textViewDescription?.text = it
        })

        movieViewModel.poster_path.observe(this, Observer {
            Glide.with(this).load(imageUrl+it).into(imageView!!);
        })

    }

    private fun fetchMovie(sUrl: String): Movie? {

        var movie: Movie? = null

        lifecycleScope.launch(Dispatchers.IO) {
            val result = getRequest(sUrl)
            if (result != null) {
                try {
                    // Parse result string JSON to data class
                    movie = Klaxon().parse<Movie>(result)

                    withContext(Dispatchers.Main) {
                        // Update view model
                        movieViewModel.original_title.value = movie?.original_title
                        movieViewModel.overview.value = movie?.overview
                        movieViewModel.poster_path.value = movie?.poster_path
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
        return movie
    }


}