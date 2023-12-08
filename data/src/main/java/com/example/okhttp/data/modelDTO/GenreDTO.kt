package com.example.okhttp.data.modelDTO

import com.example.okhttp.domain.model.Genre

data class GenreDTO(
    val id: Int,
    val name: String
){
    fun toDomain(): Genre = Genre(
        id = id,
        name = name
    )
}