package com.example.appcolegioclass.retrofit.dao

import com.example.appcolegioclass.retrofit.entidades.Menu
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiServiceMenu {
    @GET("/menu/lista")
    suspend fun listarMenus(): List<Menu>

    @GET("/menu/buscar/{cod}")
    suspend fun buscarPorCodigo(@Path("cod") codigo: Int): Menu

    @POST("/menu/registrar")
    suspend fun registrarMenu(@Body bean: Menu)

    @DELETE("/menu/eliminar/{id}")
    suspend fun eliminarMenu(@Path("id") id: Int)

    @PUT("/menu/actualizar")
    suspend fun actualizarMenu(@Body bean: Menu)
}