package com.example.okhttp.models

sealed class ListItem {

    data class MovieItem(val movie: Movie): ListItem()
    data class AdItem(val ad: Ad) : ListItem()

}