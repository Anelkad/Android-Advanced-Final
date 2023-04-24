package com.example.okhttp.models

class MovieItem{
    var id: Int = 0
    var title: String =""
    var overview: String = ""
    var release_date: String = ""
    var poster_path: String = ""
    var backdrop_path: String = ""
    var vote_average: Float = 0F

    constructor()

    constructor(
        id: Int,
        title: String,
        overview: String,
        release_date: String,
        poster_path: String,
        backdrop_path: String,
        vote_average: Float
    ){
        this.id = id
        this.title = title
        this.overview = overview
        this.release_date = release_date
        this.poster_path = poster_path
        this.backdrop_path = backdrop_path
        this.vote_average = vote_average
    }
    override fun toString(): String {
        return title
    }
}