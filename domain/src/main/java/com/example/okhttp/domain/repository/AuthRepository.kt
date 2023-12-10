package com.example.okhttp.domain.repository

import com.example.core.utils.CommonResult

interface AuthRepository {
    suspend fun login(username: String, password: String): CommonResult<Boolean>?
    suspend fun getNewToken(): CommonResult<String>
    suspend fun getNewSession(): CommonResult<String>
    suspend fun getUserId(): CommonResult<String>
    fun getUid(): String
    fun getToken(): String
    fun getUsername(): String
    fun getSession(): String
    fun isAccessTokenEmpty(): Boolean
    fun clearSession()
}