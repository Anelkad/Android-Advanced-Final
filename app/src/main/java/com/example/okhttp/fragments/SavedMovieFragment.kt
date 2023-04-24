package com.example.okhttp.fragments

import FIREBASE_URL
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.okhttp.R
import com.example.okhttp.adapters.MovieAdapter
import com.example.okhttp.adapters.SavedMovieAdapter
import com.example.okhttp.databinding.FragmentSavedMovieBinding
import com.example.okhttp.models.MovieItem
import com.google.firebase.database.*

class SavedMovieFragment : Fragment(R.layout.fragment_saved_movie) {

    lateinit var movieList: ArrayList<MovieItem>
    private lateinit var database: DatabaseReference
    lateinit var binding: FragmentSavedMovieBinding
    lateinit var movieAdapter: SavedMovieAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        movieList = ArrayList()
        binding = FragmentSavedMovieBinding.inflate(inflater,container, false)
        movieAdapter = SavedMovieAdapter(movieList)
        binding.listView.adapter = movieAdapter
        loadMovieList()
        return binding.root
    }

    private fun loadMovieList() {
        binding.prgBarMovies.isVisible = true

        database = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("movies")

        database.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                movieList.clear()
                for (ds in snapshot.children){
                    val movie = ds.getValue(MovieItem::class.java)

                    if (movie!=null){
                        movieList.add(movie)
                        Log.d("movie add", movie.title)
                    }
                }
                movieAdapter.notifyDataSetChanged()
                binding.prgBarMovies.isVisible = false
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


}