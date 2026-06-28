package com.example.appcolegioclass.retrofit

import com.example.appcolegioclass.retrofit.dao.ApiServiceAlumno
import com.example.appcolegioclass.retrofit.dao.ApiServiceMenu
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.UUID

object RetrofitClient {

    // --- INFRAESTRUCTURA: Cliente HTTP con Logging e Idempotencia ---
    private val okHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        OkHttpClient.Builder()
            .addInterceptor(logging) // Logging de operaciones concurrentes
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    // Idempotencia: Token único por petición para evitar duplicados en reintentos
                    .addHeader("X-Idempotency-Key", UUID.randomUUID().toString())
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://sistemas2026.onrender.com")
            .client(okHttpClient)
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