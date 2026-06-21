package com.example.appcolegioclass.retrofit

import com.example.appcolegioclass.retrofit.dao.ApiServiceAlumno
import com.example.appcolegioclass.retrofit.dao.ApiServiceMenu
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // --- SEGURIDAD: Base URL del backend. Si cambias de servidor, actualiza esta dirección ---
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://sistemas2026.onrender.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //Variables
    val menuApi: ApiServiceMenu by lazy {
        retrofit.create(ApiServiceMenu::class.java)
    }

    val alumnoApi: ApiServiceAlumno by lazy {
        retrofit.create(ApiServiceAlumno::class.java)
    }
}