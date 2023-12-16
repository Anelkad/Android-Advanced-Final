package com.example.core.utils

import java.util.Locale

object LanguageConstants {
    private val languageMap = mapOf("en" to "en-US", "ru" to "ru-RU")
    val LANGUAGE = languageMap[Locale.getDefault().language]
}
object ApiConstants {
    const val API_KEY = "7754ef3c3751d04070c226b198665358"
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val IMAGE_URL = "https://image.tmdb.org/t/p/original"
    const val SESSION_ID = "fcec897e35139947ca9e165fb5c153b91b24c060"

    const val GENRE_ANIMATION = "16"
    const val GENRE_DRAMA = "18"
}

object SharedPrefsConstants {
    const val REGULAR_SHARED_PREFERENCES = "REGULAR_SHARED_PREFERENCES_TOTEM"
    const val ENCRYPTED_SHARED_PREFERENCES = "ENCRYPTED_SHARED_PREFERENCES_TOTEM"

    const val ARG_TOKEN = "token"
    const val ARG_UID = "user_id"
    const val ARG_USERNAME = "username"
    const val ARG_SESSION = "session_id"
}
object AppModuleConstants{
    const val MOVIE_CLIENT = "MOVIE_CLIENT"
    const val AUTH_CLIENT = "AUTH_CLIENT"
    const val KEY_INTERCEPTOR = "KEY_INTERCEPTOR"
    const val LOGGING_INTERCEPTOR = "LOGGING_INTERCEPTOR"
}

object IntentConstants {
    const val MOVIE_ID = "MOVIE_ID"
    const val RATING = "RATING"
}
object Screen {
    const val SCREEN = "SCREEN"
    const val AUTH = "AUTH"
    const val SPLASH = "SPLASH"
}