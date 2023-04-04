package com.example.okhttp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PopularViewModel : ViewModel() {
    val page = MutableLiveData<Int>()
    val results = MutableLiveData<List<Movie>>()
}

class MovieViewModel : ViewModel() {
    val original_title = MutableLiveData<String>()
    val overview = MutableLiveData<String>()
    val poster_path = MutableLiveData<String>()
}