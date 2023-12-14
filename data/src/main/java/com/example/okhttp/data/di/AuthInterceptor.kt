package com.example.okhttp.data.di

import API_KEY
import LANGUAGE
import com.example.okhttp.data.local.SessionManager
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection

class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (!sessionManager.isAccessSessionEmpty()) makeRequest(chain)
        else chain.proceedDeletingTokenOnError(chain.request())
    }

    private fun makeRequest(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl: HttpUrl = original.url
        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("api_key", API_KEY)
            .addQueryParameter("language", LANGUAGE)
            .addQueryParameter("session_id", getSession())
            .build()
        val request = original.newBuilder()
            .url(url)
            .build()
        val response = chain.proceed(request)
        return if (response.code == HttpsURLConnection.HTTP_UNAUTHORIZED) {
            response.close()
            synchronized(this) {
                chain.proceedDeletingTokenOnError(chain.request())
            }
        } else response
    }

    private fun Interceptor.Chain.proceedDeletingTokenOnError(request: Request): Response {
        val response = proceed(request)
        if (response.code == HttpsURLConnection.HTTP_UNAUTHORIZED ||
            response.code == HttpsURLConnection.HTTP_FORBIDDEN
        ) {
            sessionManager.clearSession()
        }
        return response
    }

    private fun getSession(): String = sessionManager.session
    private fun getUid(): String = sessionManager.uid
}