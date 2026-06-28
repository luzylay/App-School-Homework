package com.example.appcolegioclass.pantallas

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.appcolegioclass.retrofit.RetrofitClient
import com.example.appcolegioclass.retrofit.entidades.Alumno
import com.example.appcolegioclass.util.SnackbarManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException

/**
 * Pantalla principal para la visualización y gestión de Alumnos.
 * 
 * Se han implementado mejoras de nivel profesional:
 * - Control de Concurrencia Optimista (Simulado mediante campo version).
 * - Optimistic UI con Rollback (Eliminación visual inmediata).
 * - Polling automático (Refresco cada 60s).
 * - Manejo de errores 409 Conflict.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaAlumno(
    addAlumno: () -> Unit,
    datosAlumno: (Int) -> Unit,
    verDocentes: () -> Unit,
    verCursos: () -> Unit,
    verAlumnos: () -> Unit,
    verMenus: () -> Unit
) {
    // --- ESTADOS Y LÓGICA DE NEGOCIO ---
    val scope = rememberCoroutineScope()
    var lista by remember { mutableStateOf<List<Alumno>>(listOf()) }
    var mostrarDialogo by remember { mutableStateOf(false) }
    var dismissActual by remember { mutableStateOf<SwipeToDismissBoxState?>(null) }
    var alumnoActual by remember { mutableStateOf<Alumno?>(null) }
    var valorBuscado by remember { mutableStateOf("") }
    
    // UX: Estado para deshabilitar botones durante operaciones
    var estaOperando by remember { mutableStateOf(false) }

    /**
     * Carga la lista de alumnos desde la API y aplica filtros locales.
     */
    fun cargarAlumnos() {
        scope.launch {
            try {
                val response = RetrofitClient.alumnoApi.listarAlumnos()
                if (response.success && response.data.isJsonArray) {
                    val listType = object : com.google.gson.reflect.TypeToken<List<Alumno>>() {}.type
                    val data: List<Alumno> = com.google.gson.Gson().fromJson(response.data, listType)
                    lista = if (valorBuscado.isEmpty()) {
                        data
                    } else {
                        data.filter {
                            it.nombre.contains(valorBuscado, ignoreCase = true) ||
                                    it.paterno.contains(valorBuscado, ignoreCase = true) ||
                                    it.materno.contains(valorBuscado, ignoreCase = true)
                        }
                    }
                }
            } catch (e: Exception) {
                // Manejo genérico de errores de red
                SnackbarManager.showMessage("Error de sincronización")
            }
        }
    }

    // SINCRONIZACIÓN: Polling automático cada 60 segundos
    LaunchedEffect(Unit) {
        while(true) {
            delay(60000)
            cargarAlumnos()
        }
    }

    // Recarga la lista automáticamente cuando el usuario escribe en el buscador
    LaunchedEffect(valorBuscado) {
        cargarAlumnos()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Alumnos", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            // --- BARRA DE NAVEGACIÓN INFERIOR PERSONALIZADA ---
            // Se implementa con scroll horizontal para soportar múltiples opciones
            BottomAppBar(
                containerColor = Color(0xFFF5F5F5), // Gris corporativo claro
                contentColor = Color(0xFF003366)    // Azul marino institucional
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val navItems = listOf(
                        "Inicio" to verDocentes,
                        "Docentes" to verDocentes,
                        "Cursos" to verCursos,
                        "Alumnos" to verAlumnos,
                        "Menús" to verMenus
                    )
                    navItems.forEach { (label, action) ->
                        TextButton(
                            onClick = action,
                            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF003366))
                        ) {
                            Text(
                                text = label,
                                // Resalta visualmente la sección activa
                                fontWeight = if (label == "Alumnos") FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { addAlumno() }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir nuevo alumno")
            }
        }
    ) { espacio ->
        Column(
            modifier = Modifier
                .padding(espacio)
                .padding(horizontal = 15.dp, vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = valorBuscado,
                onValueChange = { valorBuscado = it },
                label = { Text("Buscar") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) }
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 15.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(lista, key = { it.codigo }) { bean ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { value ->
                            if (value == SwipeToDismissBoxValue.EndToStart) {
                                alumnoActual = bean
                                mostrarDialogo = true
                                true
                            } else {
                                false
                            }
                        }
                    )

                    LaunchedEffect(dismissState.currentValue) {
                        if (dismissState.currentValue != SwipeToDismissBoxValue.Settled) {
                            dismissActual = dismissState
                        }
                    }

                    SwipeToDismissBox(
                        state = dismissState,
                        enableDismissFromStartToEnd = false,
                        backgroundContent = {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.Red)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.White)
                                }
                            }
                        }
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { datosAlumno(bean.codigo) }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(5.dp),
                                ) {
                                    Text(
                                        "Código : ${bean.codigo}",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        "Nombres : ${bean.nombre}",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        "Apellidos : ${bean.paterno} ${bean.materno}",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                                AsyncImage(
                                    model = bean.foto.ifEmpty { "https://res.cloudinary.com/dfntftd2h/image/upload/v1731639352/notfound_x7zr8p.png" },
                                    contentDescription = "Foto Alumno",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .padding(start = 10.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogo = false
                scope.launch { dismissActual?.snapTo(SwipeToDismissBoxValue.Settled) }
            },
            title = { Text("Confirmación") },
            text = { Text("¿Seguro de eliminar al alumno ${alumnoActual?.nombre}?") },
            confirmButton = {
                Button(
                    enabled = !estaOperando, // UX: Deshabilitar botón durante la operación
                    onClick = {
                        scope.launch {
                            alumnoActual?.let { seleccionado ->
                                estaOperando = true
                                val listaOriginal = lista // Respaldo para Rollback
                                
                                try {
                                    // Optimistic UI: Eliminación visual inmediata
                                    lista = lista.filter { it.codigo != seleccionado.codigo }
                                    SnackbarManager.showMessage("Borrando...") 
                                    
                                    // --- APP ANDROID: Envío de versión en el cuerpo (Concurrencia Optimista) ---
                                    RetrofitClient.alumnoApi.eliminarAlumno(seleccionado.codigo, seleccionado.version)
                                    
                                    SnackbarManager.showMessage("Alumno eliminado con éxito")
                                    mostrarDialogo = false
                                } catch (e: HttpException) {
                                    // Manejo de Concurrencia: Error 409 Conflict
                                    if (e.code() == 409) {
                                        SnackbarManager.showMessage("Conflicto: El registro fue modificado por otro usuario")
                                    } else {
                                        SnackbarManager.showMessage("Error de servidor")
                                    }
                                    lista = listaOriginal // Rollback visual
                                } catch (e: Exception) {
                                    SnackbarManager.showMessage("Error de conexión: Restaurando datos")
                                    lista = listaOriginal // Rollback visual
                                } finally {
                                    estaOperando = false
                                    cargarAlumnos() // Sincronización final
                                }
                            }
                        }
                    }
                ) {
                    if (estaOperando) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Text("Sí")
                    }
                }
            },
            dismissButton = {
                OutlinedButton(
                    enabled = !estaOperando,
                    onClick = {
                        mostrarDialogo = false
                        scope.launch { dismissActual?.snapTo(SwipeToDismissBoxValue.Settled) }
                    }
                ) { Text("No") }
            }
        )
    }
}
