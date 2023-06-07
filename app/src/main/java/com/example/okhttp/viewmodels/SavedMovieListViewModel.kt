package com.example.okhttp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okhttp.models.MovieItem
import com.example.okhttp.repository.SavedMovieRepository
import com.example.okhttp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedMovieListViewModel @Inject constructor (
    private val repository: SavedMovieRepository
) : ViewModel() {

    private val _savedMovieList = MutableLiveData<ArrayList<MovieItem>>(null)
    val savedMovieList: LiveData<ArrayList<MovieItem>> =_savedMovieList

    private val _saveMovieState = MutableLiveData<Resource<MovieItem>>(null)
    val saveMovieState: LiveData<Resource<MovieItem>> = _saveMovieState

    private val _deleteMovieState = MutableLiveData<Resource<Int>>(null)
    val deleteMovieState: LiveData<Resource<Int>> = _deleteMovieState

    init {
        getMovieList()
    }

    fun getMovieList() = viewModelScope.launch {
       repository.getSavedMovieList().collect{
           when (it) {
               is Resource.Success -> {
                   _savedMovieList.value = it.getSuccessResult()
               }
               is Resource.Failure -> {
                   Log.d("product list view model", "error")
               }
               else -> Unit
           }
       }
    }


    fun deleteMovie(movieId: Int) = viewModelScope.launch {
        _deleteMovieState.value = Resource.Loading
        val result = repository.deleteMovie(movieId)
        _deleteMovieState.value = result
    }

    fun saveMovie(movie: MovieItem) = viewModelScope.launch {
        _saveMovieState.value = Resource.Loading
        val result = repository.saveMovie(movie)
        _saveMovieState.value = result
    }
}
