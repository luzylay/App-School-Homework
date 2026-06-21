package com.example.appcolegioclass.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun createImageUri(context: Context, fileName: String = "camera_photo.jpg"): Uri {
    val imageFile = File(
        context.cacheDir,
        fileName
    )
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        imageFile
    )
}
