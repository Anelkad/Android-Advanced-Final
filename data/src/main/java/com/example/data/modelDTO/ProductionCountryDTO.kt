package com.example.data.modelDTO

import com.example.okhttp.domain.ProductionCountry


data class ProductionCountryDTO(
    val iso_3166_1: String,
    val name: String
){
    fun toDomain(): ProductionCountry = ProductionCountry(
        iso_3166_1 = iso_3166_1,
        name = name
    )
}