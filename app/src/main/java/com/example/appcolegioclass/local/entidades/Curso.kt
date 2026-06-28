package com.example.appcolegioclass.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cursos")
data class Curso(
    @PrimaryKey(autoGenerate = true) 
    val codigo: Int = 0,
    val nombre: String,
    val ciclo: String,
    val creditos: Double,
    val carrera: String,
    // --- APP ANDROID: Versionado para control de concurrencia en base de datos local ---
    val version: Int = 0
)
