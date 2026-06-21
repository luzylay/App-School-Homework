package com.example.appcolegioclass.retrofit.dao

import com.example.appcolegioclass.retrofit.entidades.Alumno
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiServiceAlumno {
    @GET("/alumno/lista")
    suspend fun listarAlumnos(): List<Alumno>

    @POST("/alumno/registrar")
    suspend fun registrarAlumno(@Body bean: Alumno)

    @DELETE("/alumno/eliminar/{id}")
    suspend fun eliminarAlumno(@Path("id") id: Int)

    @PUT("/alumno/actualizar")
    suspend fun actualizarAlumno(@Body bean: Alumno)

    @GET("/alumno/buscar/{id}")
    suspend fun buscarAlumno(@Path("id") id: Int): Alumno
}
