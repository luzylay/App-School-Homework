package com.example.appcolegioclass.utils

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

fun uriToMultipart(context:Context,uri:Uri):MultipartBody.Part{
    val inputStream =
        context.contentResolver.openInputStream(uri)
    val bytes = inputStream!!.readBytes()
    val requestBody =
        bytes.toRequestBody(
            "image/*".toMediaType()
        )
    return MultipartBody.Part.createFormData(
        "file",
        "foto.jpg",
        requestBody
    )
}
