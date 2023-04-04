package com.example.okhttp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL
import com.beust.klaxon.Klaxon
import com.bumptech.glide.Glide
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    // Create OkHttp Client
    var client: OkHttpClient = OkHttpClient();

    var textViewTitle: TextView? = null
    var textViewDescription: TextView? = null
    var buttonFetch: Button? = null
    var imageView: ImageView? = null
    val movieViewModel: MovieViewModel by viewModels()
    val popularViewModel: PopularViewModel by viewModels()

    val baseUrl: String = "https://api.themoviedb.org/3/movie/popular?api_key=7754ef3c3751d04070c226b198665358&language=en-US"
    val imageUrl = "https://image.tmdb.org/t/p/w500/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageview)
        textViewTitle = findViewById(R.id.textview_title)
        textViewDescription = findViewById(R.id.textview_description)
        buttonFetch = findViewById(R.id.button_fetch)

        buttonFetch?.setOnClickListener(View.OnClickListener {
            // Launch get request
            fetch(baseUrl)
        })

        observeViewModel()
    }

    private fun observeViewModel(){
        //observeViewModel()
        popularViewModel.results.observe(this, Observer {
            var str: String = ""
            for (i in it){
                str+=i.original_title + " "
            }
            textViewTitle?.text = str
        })
    }

    private fun getRequest(sUrl: String): String? {
        var result: String? = null
        try {
            // Create URL
            val url = URL(sUrl)
            // Build request
            val request = Request.Builder().url(url).build()
            // Execute request
            val response = client.newCall(request).execute()
            result = response.body?.string()
            Log.d("qwerty: ", result!!)
        }
        catch(err:Error) {
            print("Error when executing get request: "+err.localizedMessage)
        }
        return result
    }

    private fun fetch(sUrl: String): Popular? {

        var popular: Popular? = null

        lifecycleScope.launch(Dispatchers.IO) {
            val result = getRequest(sUrl)
            if (result != null) {
                try {
                    // Parse result string JSON to data class
                    popular = Klaxon().parse<Popular>(result)

                    withContext(Dispatchers.Main) {
                        // Update view model
                        popularViewModel.page.value = popular?.page
                        popularViewModel.results.value = popular?.results
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
        return popular
    }


}