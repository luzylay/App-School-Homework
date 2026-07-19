package com.example.appcolegioclass.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appcolegioclass.firebase.entidades.Libro
import com.example.appcolegioclass.firebase.viewmodel.LibroViewModel
import kotlinx.coroutines.launch

/**
 * PANTALLA: Adicionar Libro
 * 
 * Esta pantalla permite al usuario registrar un nuevo libro en Firebase Firestore.
 * Sigue el patrón MVVM y utiliza Jetpack Compose para la UI.
 * 
 * @param onBack Callback para navegar hacia atrás en el flujo de la aplicación.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdicionarLibro(onBack: () -> Unit) {

    // --- ESTADOS DE LA INTERFAZ (Campos de texto) ---
    var isbn by remember { mutableStateOf("") }
    var titulo by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }

    // --- GESTIÓN DE SNACKBAR (Notificaciones) ---
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // --- INTEGRACIÓN CON VIEWMODEL ---
    val viewLib: LibroViewModel = viewModel()
    val men by viewLib.mensaje.collectAsState()

    // --- ESCUCHA DE EVENTOS (Mensajes de éxito/error) ---
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
            // Limpiamos el mensaje en el ViewModel para evitar repeticiones
            viewLib.limpiarMensaje()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Registrar Libro", fontWeight = FontWeight.Bold) },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // --- FORMULARIO DE REGISTRO ---
            OutlinedTextField(
                value = isbn,
                onValueChange = { isbn = it },
                label = { Text("ISBN del Libro") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
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
                label = { Text("Stock inicial") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Espaciador flexible para empujar el botón al fondo
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Button(
                    onClick = {
                        // Validación de campos obligatorios
                        if (isbn.isBlank() || titulo.isBlank() || precio.isBlank() || stock.isBlank()) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Por favor, complete todos los campos")
                            }
                            return@Button
                        }

                        // Conversión segura de tipos
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
                    Text("Grabar Libro", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
