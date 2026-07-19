package com.example.appcolegioclass.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appcolegioclass.firebase.entidades.Libro
import com.example.appcolegioclass.firebase.viewmodel.LibroViewModel
import kotlinx.coroutines.launch

/**
 * PANTALLA: Editar Libro
 * 
 * Permite modificar los datos de un libro existente en Firebase Firestore.
 * El ISBN actúa como identificador único y no es editable para mantener la integridad.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarLibro(onBack: () -> Unit, isbn: String) {

    // --- INTEGRACIÓN CON VIEWMODEL ---
    val viewLib: LibroViewModel = viewModel()
    val libroSeleccionado by viewLib.libroSeleccionado.collectAsState()
    val men by viewLib.mensaje.collectAsState()

    // --- ESTADOS DE LA INTERFAZ ---
    var titulo by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }

    // --- GESTIÓN DE NOTIFICACIONES ---
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // --- CARGA DE DATOS INICIAL ---
    LaunchedEffect(isbn) {
        viewLib.findById(isbn)
    }

    // --- ASIGNACIÓN DE DATOS CARGADOS ---
    LaunchedEffect(libroSeleccionado) {
        libroSeleccionado?.let {
            titulo = it.titulo
            precio = it.precio.toString()
            stock = it.stock.toString()
        }
    }

    // --- ESCUCHA DE EVENTOS ---
    LaunchedEffect(men) {
        men?.let {
            if (it == "Libro guardado con éxito") {
                // Mostramos el aviso y esperamos un momento antes de regresar
                scope.launch {
                    snackbarHostState.showSnackbar(it)
                }
                kotlinx.coroutines.delay(1500)
                onBack()
            } else {
                scope.launch {
                    snackbarHostState.showSnackbar(it)
                }
            }
            viewLib.limpiarMensaje()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Editar Libro", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { 
                        viewLib.limpiarSeleccion()
                        onBack() 
                    }) {
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ISBN deshabilitado (es la llave primaria en Firestore en este diseño)
            OutlinedTextField(
                value = isbn,
                onValueChange = { },
                label = { Text("ISBN (No editable)") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                readOnly = true
            )

            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio (S/.)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text("Stock actual") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Button(
                    onClick = {
                        if (titulo.isBlank() || precio.isBlank() || stock.isBlank()) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Por favor, complete todos los campos")
                            }
                            return@Button
                        }
                        val p = precio.toDoubleOrNull()
                        val s = stock.toIntOrNull()

                        if (p == null || s == null) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Precio y Stock deben ser valores numéricos")
                            }
                            return@Button
                        }

                        viewLib.save(Libro(isbn, titulo, p, s))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("Actualizar", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
