package com.example.okhttp

class Movie(
    val id: Int,
    val original_title: String,
    val overview: String,
    val poster_path: String
)
{
    override fun toString(): String {
        return original_title
    }
}
