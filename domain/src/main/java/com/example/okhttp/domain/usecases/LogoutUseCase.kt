package com.example.okhttp.domain.usecases

import com.example.okhttp.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    fun logout() = authRepository.clearSession()
}