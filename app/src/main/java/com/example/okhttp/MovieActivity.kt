package com.example.okhttp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide

class MovieActivity : BaseActivity() {

    var id: Int = 0
    val movieViewModel: MovieViewModel by viewModels()
    val imageUrl = "https://image.tmdb.org/t/p/original"
    val apiKey = "7754ef3c3751d04070c226b198665358"
    val language = "ru-RU"


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
        val baseUrl = "https://api.themoviedb.org/3/movie/${id}?api_key=${apiKey}&language=${language}"

        movieViewModel.fetchMovie(baseUrl)
        observeViewModel()

    }

    private fun observeViewModel(){
        showWaitDialog()
        //observeViewModel()
        movieViewModel.title.observe(this, Observer {
            textViewTitle?.text = it
            hideWaitDialog()
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
            runtime?.text = "${it/60} ч ${it%60} мин"
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


}