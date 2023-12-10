package com.example.okhttp.data.api

import com.example.okhttp.data.modelDTO.LoginRequest
import com.example.okhttp.data.modelDTO.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET

interface AuthApi {
    @GET("authentication/token/validate_with_login")
    suspend fun login(
        @Body body: LoginRequest
    ): Response<LoginResponse>
}