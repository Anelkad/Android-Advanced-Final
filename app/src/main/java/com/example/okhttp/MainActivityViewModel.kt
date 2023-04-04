package com.example.okhttp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {
    val original_title = MutableLiveData<String>()
    val overview = MutableLiveData<String>()
    val poster_path = MutableLiveData<String>()
}