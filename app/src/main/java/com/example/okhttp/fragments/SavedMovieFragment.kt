package com.example.okhttp.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.okhttp.R
import com.example.okhttp.SavedMovieListViewModel
import com.example.okhttp.adapters.SavedMovieAdapter
import com.example.okhttp.databinding.FragmentSavedMovieBinding
import com.example.okhttp.models.Movie
import com.example.okhttp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedMovieFragment : Fragment() {

    lateinit var movieList: ArrayList<Movie>
    lateinit var binding: FragmentSavedMovieBinding
    lateinit var movieAdapter: SavedMovieAdapter
    val savedMovieListViewModel: SavedMovieListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        movieList = ArrayList()
        movieAdapter = SavedMovieAdapter()

        savedMovieListViewModel.getMovieList()

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

        movieAdapter.setDeleteMovieClickListener {deleteMovie(it)}
        loadMovieList()
        return binding.root
    }

    private fun loadMovieList() {
        savedMovieListViewModel.savedMovieList.observe(viewLifecycleOwner, Observer {
            movieList.clear()
            binding.progressBar.isVisible = true
            if (it!=null) {
                binding.progressBar.isVisible = false
                movieList.addAll(it)
                movieAdapter.submitList(movieList.toMutableList())
                binding.noSavedMovie.isVisible = movieList.isEmpty()
            }
        })
    }

    private fun deleteMovie(movieId: Int){
        savedMovieListViewModel.deleteMovie(movieId)
        savedMovieListViewModel.deleteMovieState.observe(viewLifecycleOwner, Observer {
            when (it){
                is Resource.Failure -> {
                    hideWaitDialog()
                    Toast.makeText(
                        context, "Cannot delete movie!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                is Resource.Loading -> {
                    showWaitDialog()
                }
                is Resource.Success ->{
                    hideWaitDialog()
                    Toast.makeText(
                        context, "Movie deleted!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> Unit
            }
        })
    }

    private lateinit var waitDialog: Dialog
    private fun showWaitDialog(){
        if (!this::waitDialog.isInitialized) {
            waitDialog = Dialog(requireActivity())
            waitDialog.setContentView(R.layout.wait_dialog)

            waitDialog.setCancelable(false)
            waitDialog.setCanceledOnTouchOutside(false)
        }
        if (!waitDialog.isShowing) waitDialog.show()
    }

    private fun hideWaitDialog(){
        if (this::waitDialog.isInitialized or waitDialog.isShowing) waitDialog.dismiss()
    }
}