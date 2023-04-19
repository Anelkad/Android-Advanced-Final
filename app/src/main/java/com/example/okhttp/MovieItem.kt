package com.example.okhttp

class MovieItem(
    val id: Int,
    val title: String,
    val overview: String,
    val release_date: String,
    val poster_path: String,
    val backdrop_path: String,
    val vote_average: Float
)
{
    override fun toString(): String {
        return title
    }
}