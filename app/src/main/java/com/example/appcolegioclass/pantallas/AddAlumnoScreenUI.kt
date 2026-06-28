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
fun AdicionarAlumno(onBack: () -> Unit) {
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
    
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageCaptured by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageCaptured = true
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Alumno", fontWeight = FontWeight.Bold) },
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
                        contentDescription = "Vista previa",
                        modifier = Modifier.size(200.dp).padding(top = 10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    scope.launch {
                        try {
                            var urlImagen = "https://res.cloudinary.com/dfntftd2h/image/upload/v1717614821/notfound_x7zr8p.png"
                            
                            if (imageCaptured && imageUri != null) {
                                val filePart = uriToMultipart(context, imageUri!!)
                                val params = mutableMapOf<String, RequestBody>()
                                params["upload_preset"] = "alumno_preset".toRequestBody("text/plain".toMediaType())
                                params["folder"] = "alumnos".toRequestBody("text/plain".toMediaType())
                                
                                val response = CloudinaryClient.api.uploadImage(filePart, params)
                                urlImagen = response.secure_url
                            }

                            val nuevoAlumno = Alumno(
                                codigo = 0,
                                nombre = nombre,
                                paterno = paterno,
                                materno = materno,
                                sexo = sexo,
                                fechaNacimiento = fechaNacimiento,
                                numeroHermanos = numeroHermanos.toIntOrNull() ?: 0,
                                foto = urlImagen
                            )
                            RetrofitClient.alumnoApi.registrarAlumno(nuevoAlumno)
                            SnackbarManager.showMessage("Alumno registrado correctamente")
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
            ) { Text("Grabar", fontWeight = FontWeight.Bold) }
        }
    }
}
