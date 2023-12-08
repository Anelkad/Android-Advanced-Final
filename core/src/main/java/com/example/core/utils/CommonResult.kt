package com.example.core.utils

data class CommonResult<out T : Any>(
    val result: T? = null,
    val error: String? = null
)