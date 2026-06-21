package com.example.appcolegioclass.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "docentes")
data class Docente(
    @PrimaryKey(autoGenerate = true)
    val codigo: Int = 0,
    val nombres: String,
    val apellidos: String,
    val sexo: String,
    val sueldo: Double,
    val hijos: Int,
    val foto: String = ""
)
