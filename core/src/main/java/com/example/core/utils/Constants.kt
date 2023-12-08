import java.util.Locale

val languageMap = mapOf("en" to "en-US", "ru" to "ru-RU")

const val API_KEY = "7754ef3c3751d04070c226b198665358"
val LANGUAGE = languageMap[Locale.getDefault().language]
const val BASE_URL = "https://api.themoviedb.org/3/"
const val IMAGE_URL = "https://image.tmdb.org/t/p/original"
const val SESSION_ID = "528be1ed672fb88b9bdf957555c11e0c5d34e0a0"
const val GENRE_ANIMATION = "16"
const val GENRE_DRAMA = "18"
