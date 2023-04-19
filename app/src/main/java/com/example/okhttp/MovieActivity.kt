package com.example.okhttp

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
    val imageUrl = "https://image.tmdb.org/t/p/original"
    val apiKey = "7754ef3c3751d04070c226b198665358"


    var textViewTitle: TextView? = null
    var textViewDescription: TextView? = null
    var tagline: TextView? = null
    var release_date: TextView? = null
    var runtime: TextView? = null
    var revenue: TextView? = null
    var imageView: ImageView? = null
    var imageView2: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        imageView = findViewById(R.id.imageview)
        imageView2 = findViewById(R.id.imageview2)
        textViewTitle = findViewById(R.id.textview_title)
        textViewDescription = findViewById(R.id.textview_description)
        tagline = findViewById(R.id.tagline)
        runtime = findViewById(R.id.runtime)
        revenue = findViewById(R.id.revenue)
        release_date = findViewById(R.id.release_date)

        id = intent.getIntExtra("id", 0)
        val baseUrl = "https://api.themoviedb.org/3/movie/${id}?api_key=${apiKey}&language=ru-RU"

        fetchMovie(baseUrl)
        observeViewModel()

    }

    private fun observeViewModel(){
        //observeViewModel()
        movieViewModel.title.observe(this, Observer {
            textViewTitle?.text = it
        })

        movieViewModel.overview.observe(this, Observer {
            textViewDescription?.text = it
        })

        movieViewModel.tagline.observe(this, Observer {
            if(it.isNotEmpty()) tagline?.text = "\"${it}\""
        })

        movieViewModel.release_date.observe(this, Observer {
            release_date?.text = "Премьера: ${it}"
        })

        movieViewModel.runtime.observe(this, Observer {
            runtime?.text = "${it/60} час ${it%60} минут"
        })

        movieViewModel.revenue.observe(this, Observer {
            if (it>0) revenue?.text = "Кассовые сборы: ${it/1000000} млн $"
        })

        movieViewModel.poster_path.observe(this, Observer {
            Glide
                .with(this)
                .load(imageUrl+it)
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.baseline_image_24)
                .into(imageView!!);
        })

        movieViewModel.backdrop_path.observe(this, Observer {
            Glide
                .with(this)
                .load(imageUrl+it)
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.baseline_image_24)
                .into(imageView2!!);
        })

    }

    private fun fetchMovie(sUrl: String): Movie? {
        var movie: Movie? = null
        showWaitDialog()

        lifecycleScope.launch(Dispatchers.IO) {
            val result = getRequest(sUrl)
            if (result != null) {
                try {
                    // Parse result string JSON to data class
                    movie = Klaxon().parse<Movie>(result)

                    withContext(Dispatchers.Main) {
                        // Update view model
                        movieViewModel.title.value = movie?.title
                        movieViewModel.overview.value = movie?.overview
                        movieViewModel.release_date.value = movie?.release_date
                        movieViewModel.vote_average.value = movie?.vote_average
                        movieViewModel.poster_path.value = movie?.poster_path
                        movieViewModel.backdrop_path.value = movie?.backdrop_path
                        movieViewModel.tagline.value = movie?.tagline
                        movieViewModel.runtime.value = movie?.runtime
                        movieViewModel.revenue.value = movie?.revenue
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
        return movie
    }


}