package com.example.appcolegioclass.retrofit

import com.example.appcolegioclass.retrofit.dao.CloudinaryApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue

object CloudinaryClient {

    // --- SEGURIDAD: Base URL de la API de Cloudinary. No suele cambiar. ---
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.cloudinary.com/")
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
    }

    val api: CloudinaryApi by lazy {
        retrofit.create(CloudinaryApi::class.java)
    }
}