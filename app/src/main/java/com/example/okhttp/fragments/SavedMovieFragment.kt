package com.example.okhttp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.okhttp.R
import com.example.okhttp.SavedMovieListViewModel
import com.example.okhttp.adapters.SavedMovieAdapter
import com.example.okhttp.databinding.FragmentSavedMovieBinding
import com.example.okhttp.models.MovieItem

class SavedMovieFragment : Fragment() {

    lateinit var movieList: ArrayList<MovieItem>
    lateinit var binding: FragmentSavedMovieBinding
    lateinit var movieAdapter: SavedMovieAdapter
    val savedMovieListViewModel: SavedMovieListViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        movieList = ArrayList()
        movieAdapter = SavedMovieAdapter(movieList, savedMovieListViewModel)

        savedMovieListViewModel.fetchList()

        binding = FragmentSavedMovieBinding.inflate(inflater,container, false)
        binding.listView.adapter = movieAdapter

        movieAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putInt("id", it)
            }
            findNavController().navigate(
                R.id.action_savedMovieFragment_to_movieDetailsFragment,
                bundle
            )
        }

        loadMovieList()
        return binding.root
    }

    private fun loadMovieList() {

        savedMovieListViewModel.results.observe(viewLifecycleOwner, Observer {
            movieList.clear()
            binding.prgBarMovies.isVisible = true
            movieList.addAll(it)
            binding.noSavedMovie.isVisible = movieList.size==0

            movieAdapter.notifyDataSetChanged()

            binding.prgBarMovies.isVisible = false

            if(movieList.size>0)Log.d("observe_firebase: ", "${movieList[movieList.size-1].title}: ${movieList.size}")
        })

    }


}