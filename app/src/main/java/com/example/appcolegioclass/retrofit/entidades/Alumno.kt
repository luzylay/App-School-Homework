package com.example.appcolegioclass.retrofit.entidades

data class Alumno(
    val codigo: Int,
    val nombre: String,
    val paterno: String,
    val materno: String,
    val sexo: String,
    val fechaNacimiento: String,
    val numeroHermanos: Int,
    val foto: String,
    // --- APP ANDROID: Mapeo de versión para control de concurrencia optimista ---
    val version: Int = 1
)
