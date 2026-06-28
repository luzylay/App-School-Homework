package com.example.appcolegioclass.pantallas

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.example.appcolegioclass.retrofit.CloudinaryClient
import com.example.appcolegioclass.util.SnackbarManager
import com.example.appcolegioclass.retrofit.RetrofitClient
import com.example.appcolegioclass.retrofit.entidades.ErrorResponse
import com.example.appcolegioclass.retrofit.entidades.Menu
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
fun AdicionarMenu(
    // --- FLUJO DE NAVEGACIÓN: Callback para retornar a la lista ---
    onBack: () -> Unit
) {
    // --- FLUJO: Ámbito de corrutina para realizar operaciones suspendidas (DB) ---
    val scope = rememberCoroutineScope()

    // --- FLUJO DE ESTADO: Variables reactivas para capturar la entrada del usuario ---
    var nombre by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("0") }
    var precio by remember { mutableStateOf("0.0") }
    val categorias = listOf("Entradas", "Platos de Fondo", "Comida Criolla","Menú Ejecutivo","Comida Vegetariana")
    var expanded by remember { mutableStateOf(false) }
    var nomCategoria by remember { mutableStateOf("") }

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

    // --- ESTILO Y ESTRUCTURA: Contenedor principal ---
    Scaffold(
        topBar = {
            // --- ESTILO: Barra superior con colores estándar del proyecto ---
            TopAppBar(
                title = { Text("Registrar Menú", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    // --- FLUJO: Acción de retroceso ---
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
        // --- ESTILO Y ESTRUCTURA: Formulario de entrada ---
        Column(
            modifier = Modifier
                .padding(espacio)
                .padding(15.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // --- ESTILO: Campos de texto con borde (Outlined) ---
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Ingresar nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            // --- ESTILO Y FLUJO: Selector desplegable para Categoría ---
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

            Column(

            ) {
                Button(
                    onClick = {
                        val uri = createImageUri(context)
                        imageUri = uri
                        launcher.launch(uri)
                    }
                ) {
                    Text("Tomar foto")
                }
                if (imageCaptured && imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier.size(250.dp)
                    )
                }
            }

            // --- FLUJO DE DATOS: Botón para persistir el menú ---
            Button(
                onClick = {
                    scope.launch {
                        try {
                            var urlImagen = "https://res.cloudinary.com/dfntftd2h/image/upload/v1731639352/notfound_x7zr8p.png"
                            
                            if (imageCaptured && imageUri != null) {
                                val filePart = uriToMultipart(context, imageUri!!)
                                val params = mutableMapOf<String, RequestBody>()
                                params["upload_preset"] = "menu_preset".toRequestBody("text/plain".toMediaType())
                                params["folder"] = "menus".toRequestBody("text/plain".toMediaType())
                                
                                val response = CloudinaryClient.api.uploadImage(filePart, params)
                                urlImagen = response.secure_url
                            }

                            RetrofitClient.menuApi.registrarMenu(
                                Menu(
                                    0,
                                    nombre,
                                    nomCategoria,
                                    stock.toIntOrNull() ?: 0,
                                    precio.toDoubleOrNull() ?: 0.0,
                                    urlImagen
                                )
                            )
                            SnackbarManager.showMessage("Menú registrado correctamente")
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
                shape = RoundedCornerShape(5.dp) // ESTILO: Bordes rectangulares suaves
            ) {
                Text("Grabar", fontWeight = FontWeight.Bold)
            }
        }
    }
}
