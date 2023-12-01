package com.example.data.modelDTO

import com.example.okhttp.domain.Genre

data class GenreDTO(
    val id: Int,
    val name: String
){
    fun toDomain(): Genre = Genre(
        id = id,
        name = name
    )
}