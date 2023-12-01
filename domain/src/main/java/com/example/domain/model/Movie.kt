package com.example.domain.model

data class Movie(
    var id: Int = 0,
    var title: String = "",
    var overview: String? = null,
    var releaseDate: String? = null,
    var posterPath: String? = null,
    var backdropPath: String? = null,
    var voteAverage: Float? = null
)
//todo remove no-arg constructor
