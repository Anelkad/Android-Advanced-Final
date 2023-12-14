package com.example.okhttp.domain.usecases

import com.example.core.utils.CommonResult
import com.example.okhttp.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun login(username: String, password: String): CommonResult<Boolean>? =
        authRepository.login(username, password)

    suspend fun getNewToken(): CommonResult<String> = authRepository.getNewToken()
}