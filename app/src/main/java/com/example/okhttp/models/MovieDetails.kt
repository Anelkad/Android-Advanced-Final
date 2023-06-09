package com.example.okhttp.models


data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String,
    val release_date: String,
    val poster_path: String,
    val backdrop_path: String,
    val vote_average: Float,
    val tagline: String,
    val revenue: Int,
    val runtime: Int
){
    fun toMovie(): Movie =
        Movie(id, title, overview, release_date, poster_path, backdrop_path, vote_average)
}
