package com.example.appcolegioclass.pantallas

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.example.appcolegioclass.local.AppDatabase
import com.example.appcolegioclass.util.SnackbarManager
import com.example.appcolegioclass.local.entidades.Docente
import com.example.appcolegioclass.retrofit.CloudinaryClient
import com.example.appcolegioclass.utils.createImageUri
import com.example.appcolegioclass.utils.uriToMultipart
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdicionarDocente(
    // --- FLUJO DE NAVEGACIÓN: Callback para retornar a la lista ---
    onBack: () -> Unit,
    // --- FLUJO DE DATOS: Inyección de la base de datos ---
    db: AppDatabase
) {
    // --- FLUJO DE DATOS: Acceso al DAO para operaciones de escritura ---
    val dao = db.docenteDao()

    // --- FLUJO: Ámbito de corrutina para realizar operaciones suspendidas (DB) ---
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // --- FLUJO DE ESTADO: Variables reactivas para capturar la entrada del usuario ---
    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var sueldo by remember { mutableStateOf("") }
    var hijos by remember { mutableStateOf("") }
    val sexos = listOf("Masculino", "Femenino", "Otros")
    var expanded by remember { mutableStateOf(false) }
    var nomSexo by remember { mutableStateOf("") }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageCaptured by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
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
                title = { Text("Registrar Docente", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1976D2),
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
                value = nombres,
                onValueChange = { nombres = it },
                label = { Text("Ingresar nombres") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = apellidos,
                onValueChange = { apellidos = it },
                label = { Text("Ingresar apellidos") },
                modifier = Modifier.fillMaxWidth()
            )
            
            // --- ESTILO Y FLUJO: Selector desplegable para Sexo ---
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = nomSexo,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("[Seleccione Sexo]") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    sexos.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                nomSexo = item
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = sueldo,
                onValueChange = { sueldo = it },
                label = { Text("Ingresar sueldo") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = hijos,
                onValueChange = { hijos = it },
                label = { Text("Ingresar hijos") },
                modifier = Modifier.fillMaxWidth()
            )

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

                val imagenAMostrar = if (imageCaptured && imageUri != null) imageUri else "https://res.cloudinary.com/dfntftd2h/image/upload/v1731639352/notfound_x7zr8p.png"

                AsyncImage(
                    model = imagenAMostrar,
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- FLUJO DE DATOS: Botón para persistir el docente en la DB ---
            Button(
                onClick = {
                    scope.launch {
                        try {
                            if (nombres.isNotBlank() && apellidos.isNotBlank() && nomSexo.isNotBlank() && sueldo.isNotBlank() && hijos.isNotBlank()) {
                                // Imagen por defecto si no se toma foto
                                var urlImagen = "https://res.cloudinary.com/dfntftd2h/image/upload/v1731639352/notfound_x7zr8p.png"

                                if (imageCaptured && imageUri != null) {
                                    val filePart = uriToMultipart(context, imageUri!!)
                                    val params = mutableMapOf<String, RequestBody>()
                                    params["upload_preset"] = "docente_preset".toRequestBody("text/plain".toMediaType())
                                    params["folder"] = "docentes".toRequestBody("text/plain".toMediaType())

                                    val response = CloudinaryClient.api.uploadImage(filePart, params)
                                    urlImagen = response.secure_url
                                }

                                dao.registrar(
                                    Docente(
                                        0,
                                        nombres,
                                        apellidos,
                                        nomSexo,
                                        sueldo.toDoubleOrNull() ?: 0.0,
                                        hijos.toIntOrNull() ?: 0,
                                        urlImagen
                                    )
                                )
                                SnackbarManager.showMessage("Docente registrado")
                                onBack() // Regresa después de grabar satisfactoriamente
                            } else {
                                SnackbarManager.showMessage("Por favor complete todos los campos")
                            }
                        } catch (e: Exception) {
                            SnackbarManager.showMessage("Error: ${e.message}")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Text("Grabar Docente", fontWeight = FontWeight.Bold)
            }
        }
    }
}
