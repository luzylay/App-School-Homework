package com.example.appcolegioclass.retrofit.entidades

data class ApiResponse <T>(
    val success: Boolean,
    val mensaje: String,
    val data: T
)