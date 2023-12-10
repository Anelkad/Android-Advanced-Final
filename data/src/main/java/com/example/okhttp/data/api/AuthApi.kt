package com.example.okhttp.data.api

import com.example.okhttp.data.modelDTO.LoginRequestBody
import com.example.okhttp.data.modelDTO.LoginResponse
import com.example.okhttp.data.modelDTO.NewSessionResponse
import com.example.okhttp.data.modelDTO.NewTokenResponse
import com.example.okhttp.data.modelDTO.SessionRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("authentication/token/validate_with_login")
    suspend fun login(
        @Body body: LoginRequestBody
    ): Response<LoginResponse>

    @GET("authentication/token/new")
    suspend fun getNewToken(): Response<NewTokenResponse>

    @POST("authentication/session/new")
    suspend fun getNewSession(
        @Body body: SessionRequestBody
    ): Response<NewSessionResponse>
}