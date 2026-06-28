package com.example.appcolegioclass.retrofit.dao

import com.example.appcolegioclass.retrofit.entidades.Menu
import com.example.appcolegioclass.retrofit.entidades.ApiResponse
import com.google.gson.JsonElement
import retrofit2.http.*

interface ApiServiceMenu {
    @GET("/men/lista")
    suspend fun listarMenus(): ApiResponse<JsonElement>

    @GET("/men/buscar/{cod}")
    suspend fun buscarPorCodigo(@Path("cod") codigo: Int): ApiResponse<JsonElement>

    @POST("/men/registrar")
    suspend fun registrarMenu(@Body bean: Menu): ApiResponse<JsonElement>

    @HTTP(method = "DELETE", path = "/men/eliminar/{id}", hasBody = true)
    suspend fun eliminarMenu(@Path("id") id: Int, @Body version: Int)

    @PUT("/men/actualizar")
    suspend fun actualizarMenu(@Body bean: Menu): ApiResponse<JsonElement>
}