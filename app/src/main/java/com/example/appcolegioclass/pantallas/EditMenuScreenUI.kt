package com.example.appcolegioclass.pantallas

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.example.appcolegioclass.retrofit.CloudinaryClient
import com.example.appcolegioclass.retrofit.RetrofitClient
import com.example.appcolegioclass.retrofit.entidades.ErrorResponse
import com.example.appcolegioclass.retrofit.entidades.Menu
import com.example.appcolegioclass.util.SnackbarManager
import com.example.appcolegioclass.utils.createImageUri
import com.example.appcolegioclass.utils.uriToMultipart
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarMenu(onBack: () -> Unit, codigo: Int) {
    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val categorias = listOf("Entradas", "Segundos", "Postres", "Bebidas")
    var nomCategoria by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var foto by remember { mutableStateOf("") }
    var version by remember { mutableIntStateOf(1) }

    val context = LocalContext.current
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var imageCaptured by remember {
        mutableStateOf(false)
    }

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) {
            if (it) {
                imageCaptured = it
            }
        }

    LaunchedEffect(codigo) {
        try {
            val response = RetrofitClient.menuApi.buscarPorCodigo(codigo)
            if (response.success) {
                val gson = com.google.gson.Gson()
                val m: Menu? = when {
                    response.data.isJsonArray -> {
                        val listType = object : com.google.gson.reflect.TypeToken<List<Menu>>() {}.type
                        val menus: List<Menu> = gson.fromJson(response.data, listType)
                        menus.firstOrNull()
                    }
                    response.data.isJsonObject -> {
                        gson.fromJson(response.data, Menu::class.java)
                    }
                    else -> null
                }
                
                m?.let {
                    nombre = it.nombre
                    nomCategoria = it.categoria
                    stock = it.stock.toString()
                    precio = it.precio.toString()
                    foto = it.foto
                    version = it.version
                }
            }
        } catch (e: Exception) {
            SnackbarManager.showMessage("Error al cargar menú")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Menú", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { espacio ->
        Column(
            modifier = Modifier
                .padding(espacio)
                .padding(15.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Ingresar nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = nomCategoria,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("[Seleccione Categoría]") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categorias.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                nomCategoria = item
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text("Ingresar stock") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Ingresar precio") },
                modifier = Modifier.fillMaxWidth()
            )

            Column {
                Button(
                    onClick = {
                        // Resetear estado antes de tomar la foto
                        imageCaptured = false
                        val uri = createImageUri(context, "menu_${System.currentTimeMillis()}.jpg")
                        imageUri = uri
                        launcher.launch(uri)
                    }
                ) {
                    Text("Tomar foto")
                }
                
                // Agregamos un timestamp a la URI local para forzar el refresco de Coil
                val imagenAMostrar: Any = if (imageCaptured && imageUri != null) {
                    "${imageUri.toString()}?t=${System.currentTimeMillis()}"
                } else {
                    foto.ifEmpty { "https://res.cloudinary.com/dfntftd2h/image/upload/v1731639352/notfound_x7zr8p.png" }
                }
                
                AsyncImage(
                    model = imagenAMostrar,
                    contentDescription = null,
                    modifier = Modifier.size(250.dp)
                )
            }

            // --- FLUJO DE DATOS: Botón para actualizar el menú ---
            Button(
                onClick = {
                    scope.launch {
                        try {
                            var urlImagen = foto
                            
                            if (imageCaptured && imageUri != null) {
                                val filePart = uriToMultipart(context, imageUri!!)
                                val params = mutableMapOf<String, RequestBody>()
                                params["upload_preset"] = "menu_preset".toRequestBody("text/plain".toMediaType())
                                params["folder"] = "menus".toRequestBody("text/plain".toMediaType())
                                
                                val response = CloudinaryClient.api.uploadImage(filePart, params)
                                urlImagen = response.secure_url
                            }

                            RetrofitClient.menuApi.actualizarMenu(
                                Menu(
                                    codigo,
                                    nombre,
                                    nomCategoria,
                                    stock.toIntOrNull() ?: 0,
                                    precio.toDoubleOrNull() ?: 0.0,
                                    urlImagen,
                                    version
                                )
                            )
                            SnackbarManager.showMessage("Menú actualizado")
                            onBack()
                        } catch (e: HttpException) {
                            val errorBody = e.response()?.errorBody()?.string()
                            var mensaje = ""
                            try {
                                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                                // Caso 1: validaciones (data != null)
                                if (!errorResponse.data.isNullOrEmpty()) {
                                    val validaciones = buildString {
                                        append(errorResponse.mensaje)
                                        append("\n\n")
                                        errorResponse.data.forEach { (clave, mensaje) ->
                                            append("• $clave : $mensaje\n")
                                        }
                                    }
                                    mensaje = validaciones
                                } else {
                                    // Caso 2: error simple (ej: nombre existe)
                                    mensaje = errorResponse.mensaje
                                }
                            } catch (ex: Exception) {
                                mensaje = e.message()
                            }
                            SnackbarManager.showMessage(mensaje)
                        } catch (e: Exception) {
                            SnackbarManager.showMessage("Error: ${e.message}")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(5.dp)
            ) {
                Text("Modificar", fontWeight = FontWeight.Bold)
            }
        }
    }
}
