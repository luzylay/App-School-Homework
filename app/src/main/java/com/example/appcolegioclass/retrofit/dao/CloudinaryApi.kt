package com.example.appcolegioclass.retrofit.dao

import com.example.appcolegioclass.retrofit.entidades.CloudinaryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface CloudinaryApi {
    @Multipart
    // --- SEGURIDAD: Cambia 'dfntftd2h' por tu propio 'Cloud Name' de Cloudinary ---
    @POST("v1_1/dfntftd2h/image/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>
    ): CloudinaryResponse
}