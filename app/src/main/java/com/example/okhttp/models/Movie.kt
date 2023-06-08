package com.example.okhttp.models

data class Movie(
    var id: Int = 0,
    var title: String ="",
    var overview: String = "",
    var release_date: String = "",
    var poster_path: String = "",
    var backdrop_path: String = "",
    var vote_average: Float = 0F)