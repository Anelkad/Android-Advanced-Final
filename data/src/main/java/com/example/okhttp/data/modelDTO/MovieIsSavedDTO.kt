package com.example.okhttp.data.modelDTO

import com.example.okhttp.domain.model.MovieIsSaved
import com.example.okhttp.domain.model.Rating
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

data class MovieIsSavedDTO(
    val id: Int,
    val favorite: Boolean,
    val rated: RatingDTO?
) {
    fun toDomain(): MovieIsSaved = MovieIsSaved(
        id = this.id,
        favorite = this.favorite,
        rated = this.rated?.toDomain()
    )
}

data class RatingDTO(val value: Double) {
    fun toDomain(): Rating = Rating(value = value)
}

class RatingDeserializer : JsonDeserializer<RatingDTO> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): RatingDTO? {
        return when {
            json?.isJsonObject == true -> {
                val value = json.asJsonObject.getAsJsonPrimitive("value")?.asDouble
                RatingDTO(value ?: 0.0)
            }
            json?.isJsonPrimitive == true -> {
                null
            }
            else -> null
        }
    }
}