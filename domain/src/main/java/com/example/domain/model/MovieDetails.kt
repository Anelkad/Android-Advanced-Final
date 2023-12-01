package com.example.domain.model

data class MovieDetails(
    val id: Int,
    val title: String,
    val originalTitle: String,
    val overview: String,
    val releaseDate: String,
    val posterPath: String,
    val backdropPath: String,
    val voteAverage: Float,
    val voteCount: Int,
    val productionCountries: List<ProductionCountry>,
    val genres: List<Genre>,
    val tagline: String,
    val revenue: Int,
    val runtime: Int
){
    fun toMovie(): Movie =
        Movie(id, title, overview, releaseDate, posterPath, backdropPath, voteAverage)
}
