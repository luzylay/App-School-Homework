package com.example.appcolegioclass.retrofit.entidades

data class Menu (
    val codigo: Int,
    val nombre: String,
    val categoria: String,
    val stock: Int,
    val precio: Double,
    val foto: String,
    // --- APP ANDROID: Mapeo de versión para control de concurrencia optimista ---
    val version: Int = 1
)
