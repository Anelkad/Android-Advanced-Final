package com.example.okhttp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.beust.klaxon.Klaxon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL


class MainActivity : BaseActivity() {

    var listView: ListView? = null
    val popularViewModel: PopularViewModel by viewModels()

    val baseUrl: String = "https://api.themoviedb.org/3/movie/popular?api_key=7754ef3c3751d04070c226b198665358&language=en-US"

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

        popularViewModel.results.observe(this, Observer {

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

    private fun fetchList(sUrl: String): Popular? {

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