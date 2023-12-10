package com.example.okhttp.domain.model

data class MovieIsSaved(
    val id: Int,
    val favorite: Boolean,
    val rated: Rating?
)

data class Rating(val value: Double?)