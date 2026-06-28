package com.example.appcolegioclass.retrofit.dao

import com.example.appcolegioclass.retrofit.entidades.Alumno
import com.example.appcolegioclass.retrofit.entidades.ApiResponse
import com.google.gson.JsonElement
import retrofit2.http.*

interface ApiServiceAlumno {
    @GET("/alu/lista")
    suspend fun listarAlumnos(): ApiResponse<JsonElement>

    @POST("/alu/registrar")
    suspend fun registrarAlumno(@Body bean: Alumno): ApiResponse<JsonElement>

    @HTTP(method = "DELETE", path = "/alu/eliminar/{id}", hasBody = true)
    suspend fun eliminarAlumno(@Path("id") id: Int, @Body version: Int)

    @PUT("/alu/actualizar")
    suspend fun actualizarAlumno(@Body bean: Alumno): ApiResponse<JsonElement>

    @GET("/alu/buscar/{id}")
    suspend fun buscarAlumno(@Path("id") id: Int): ApiResponse<JsonElement>
}
