package com.example.appcolegioclass.pantallas

import android.app.DatePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import coil.compose.AsyncImage
import com.example.appcolegioclass.retrofit.CloudinaryClient
import com.example.appcolegioclass.retrofit.RetrofitClient
import com.example.appcolegioclass.retrofit.entidades.Alumno
import com.example.appcolegioclass.retrofit.entidades.ErrorResponse
import com.example.appcolegioclass.util.SnackbarManager
import com.example.appcolegioclass.utils.createImageUri
import com.example.appcolegioclass.utils.uriToMultipart
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarAlumno(onBack: () -> Unit, codigo: Int) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    
    var nombre by remember { mutableStateOf("") }
    var paterno by remember { mutableStateOf("") }
    var materno by remember { mutableStateOf("") }
    val sexos = listOf("M", "F")
    val sexosNombres = mapOf("M" to "Masculino", "F" to "Femenino")
    var expandedSexo by remember { mutableStateOf(false) }
    var sexo by remember { mutableStateOf("M") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var numeroHermanos by remember { mutableStateOf("") }
    var foto by remember { mutableStateOf("") }
    var version by remember { mutableIntStateOf(1) }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    // ... rest of state variables ...
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

    val calendario = Calendar.getInstance()
    val anio = calendario.get(Calendar.YEAR)
    val mes = calendario.get(Calendar.MONTH)
    val dia = calendario.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _, y, m, d ->
            val mesFormateado = if (m + 1 < 10) "0${m + 1}" else "${m + 1}"
            val diaFormateado = if (d < 10) "0$d" else "$d"
            fechaNacimiento = "$y-$mesFormateado-$diaFormateado"
        }, anio, mes, dia
    )

    LaunchedEffect(codigo) {
        try {
            val response = RetrofitClient.alumnoApi.buscarAlumno(codigo)
            if (response.success) {
                val gson = com.google.gson.Gson()
                val a: Alumno? = when {
                    response.data.isJsonArray -> {
                        val listType = object : com.google.gson.reflect.TypeToken<List<Alumno>>() {}.type
                        val alumnos: List<Alumno> = gson.fromJson(response.data, listType)
                        alumnos.firstOrNull()
                    }
                    response.data.isJsonObject -> {
                        gson.fromJson(response.data, Alumno::class.java)
                    }
                    else -> null
                }

                a?.let {
                    nombre = it.nombre
                    paterno = it.paterno
                    materno = it.materno
                    sexo = it.sexo
                    fechaNacimiento = it.fechaNacimiento
                    numeroHermanos = it.numeroHermanos.toString()
                    foto = it.foto
                    version = it.version
                }
            }
        } catch (e: Exception) {
            SnackbarManager.showMessage("Error al cargar datos del alumno")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Alumno", fontWeight = FontWeight.Bold) },
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
            // --- FLUJO: EDICIÓN DE ALUMNO ---
            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Ingresar nombre") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = paterno, onValueChange = { paterno = it }, label = { Text("Ingresar apellido paterno") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = materno, onValueChange = { materno = it }, label = { Text("Ingresar apellido materno") }, modifier = Modifier.fillMaxWidth())
            
            ExposedDropdownMenuBox(
                expanded = expandedSexo,
                onExpandedChange = { expandedSexo = !expandedSexo }
            ) {
                OutlinedTextField(
                    value = sexosNombres[sexo] ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("[Seleccione Sexo]") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedSexo) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded = expandedSexo,
                    onDismissRequest = { expandedSexo = false }
                ) {
                    sexos.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(sexosNombres[item] ?: "") },
                            onClick = {
                                sexo = item
                                expandedSexo = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = fechaNacimiento,
                onValueChange = { },
                label = { Text("Fecha de nacimiento") },
                modifier = Modifier.fillMaxWidth().clickable { datePickerDialog.show() },
                enabled = false,
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(Icons.Default.DateRange, contentDescription = null)
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            OutlinedTextField(value = numeroHermanos, onValueChange = { numeroHermanos = it }, label = { Text("Ingresar número hermanos") }, modifier = Modifier.fillMaxWidth())
            
            Column {
                Button(
                    onClick = {
                        // Resetear estado antes de tomar la foto
                        imageCaptured = false
                        val uri = createImageUri(context, "alumno_${System.currentTimeMillis()}.jpg")
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

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    scope.launch {
                        try {
                            if (nombre.isNotBlank() && paterno.isNotBlank() && materno.isNotBlank() && fechaNacimiento.isNotBlank()) {
                                var urlImagen = foto
                                
                                if (imageCaptured && imageUri != null) {
                                    val filePart = uriToMultipart(context, imageUri!!)
                                    val params = mutableMapOf<String, RequestBody>()
                                    params["upload_preset"] = "alumno_preset".toRequestBody("text/plain".toMediaType())
                                    params["folder"] = "alumnos".toRequestBody("text/plain".toMediaType())
                                    
                                    val response = CloudinaryClient.api.uploadImage(filePart, params)
                                    urlImagen = response.secure_url
                                }

                                val alumnoActualizado = Alumno(
                                    codigo = codigo,
                                    nombre = nombre,
                                    paterno = paterno,
                                    materno = materno,
                                    sexo = sexo,
                                    fechaNacimiento = fechaNacimiento,
                                    numeroHermanos = numeroHermanos.toIntOrNull() ?: 0,
                                    foto = urlImagen,
                                    version = version
                                )
                                RetrofitClient.alumnoApi.actualizarAlumno(alumnoActualizado)
                                SnackbarManager.showMessage("Alumno actualizado correctamente")
                                onBack()
                            } else {
                                SnackbarManager.showMessage("Complete los campos obligatorios")
                            }
                        } catch (e: HttpException) {
                            val errorBody = e.response()?.errorBody()?.string()
                            var mensajeFinal = "Error de validación"
                            try {
                                val errorType = object : com.google.gson.reflect.TypeToken<com.example.appcolegioclass.retrofit.entidades.ApiResponse<com.google.gson.JsonElement>>() {}.type
                                val errorResponse: com.example.appcolegioclass.retrofit.entidades.ApiResponse<com.google.gson.JsonElement> = Gson().fromJson(errorBody, errorType)
                                if (errorResponse != null && errorResponse.data.isJsonObject) {
                                    val errors = errorResponse.data.asJsonObject
                                    mensajeFinal = buildString {
                                        append(errorResponse.mensaje).append("\n")
                                        errors.entrySet().forEach { entry ->
                                            append("• ${entry.key}: ${entry.value.asString}\n")
                                        }
                                    }
                                } else if (errorResponse != null) {
                                    mensajeFinal = errorResponse.mensaje
                                }
                            } catch (ex: Exception) {
                                mensajeFinal = "Error inesperado del servidor"
                            }
                            SnackbarManager.showMessage(mensajeFinal)
                        } catch (e: Exception) {
                            SnackbarManager.showMessage("Error: ${e.message}")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(5.dp)
            ) { Text("Actualizar", fontWeight = FontWeight.Bold) }
        }
    }
}
