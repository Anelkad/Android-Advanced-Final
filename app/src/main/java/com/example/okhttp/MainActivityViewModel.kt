package com.example.okhttp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MovieListViewModel : ViewModel() {
    val page = MutableLiveData<Int>()
    val results = MutableLiveData<List<MovieItem>>()
}

class MovieViewModel : ViewModel() {
    val id = MutableLiveData<Int>()
    val title = MutableLiveData<String>()
    val overview = MutableLiveData<String>()
    val release_date = MutableLiveData<String>()
    val poster_path = MutableLiveData<String>()
    val backdrop_path = MutableLiveData<String>()
    val tagline = MutableLiveData<String>()
    val vote_average = MutableLiveData<Float>()
    val runtime = MutableLiveData<Int>()
    val revenue = MutableLiveData<Int>()
}