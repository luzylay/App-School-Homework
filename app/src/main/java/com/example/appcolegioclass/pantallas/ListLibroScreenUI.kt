package com.example.appcolegioclass.pantallas

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appcolegioclass.firebase.entidades.Libro
import com.example.appcolegioclass.firebase.viewmodel.LibroViewModel
import kotlinx.coroutines.launch

/**
 * PANTALLA: Lista de Libros (Firebase)
 * 
 * Muestra el catálogo de libros almacenados en Firestore.
 * Incluye funcionalidades de:
 * - Listado en tiempo real.
 * - Búsqueda por título o ISBN.
 * - Eliminación mediante gesto Swipe-to-Dismiss con confirmación.
 * - Navegación global.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLibros(
    addLibro: () -> Unit,
    verDocentes: () -> Unit,
    verCursos: () -> Unit,
    verAlumnos: () -> Unit,
    verMenus: () -> Unit,
    verLibros: () -> Unit
) {
    // --- INTEGRACIÓN CON VIEWMODEL ---
    val viewLib: LibroViewModel = viewModel()
    val lista by viewLib.libros.collectAsState()
    val scope = rememberCoroutineScope()

    // --- ESTADOS LOCALES (Búsqueda y Diálogos) ---
    var mostrarDialogo by remember { mutableStateOf(false) }
    var libroEliminar by remember { mutableStateOf<Libro?>(null) }
    var dismissActual by remember { mutableStateOf<SwipeToDismissBoxState?>(null) }
    var textoBusqueda by remember { mutableStateOf("") }

    // Cargar datos al iniciar la pantalla
    LaunchedEffect(Unit) {
        viewLib.findAll()
    }

    // Filtrado de lista en memoria para respuesta instantánea
    val librosFiltrados = lista.filter {
        it.titulo.contains(textoBusqueda, ignoreCase = true) ||
                it.isbn.contains(textoBusqueda, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de Libros", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1976D2), // Azul Material
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            // Barra de navegación personalizada
            BottomAppBar(
                containerColor = Color(0xFFF5F5F5),
                contentColor = Color(0xFF0D47A1)
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
                        "Docentes" to verDocentes,
                        "Cursos" to verCursos,
                        "Alumnos" to verAlumnos,
                        "Menús" to verMenus,
                        "Libros" to verLibros
                    )
                    navItems.forEach { (label, action) ->
                        TextButton(
                            onClick = action,
                            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF0D47A1))
                        ) {
                            Text(
                                text = label,
                                fontWeight = if (label == "Libros") FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { addLibro() },
                containerColor = Color(0xFF1976D2),
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir Libro")
            }
        }
    ) { espacio ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(espacio)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // --- BUSCADOR ---
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                placeholder = { Text("Buscar por título o ISBN...") },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // --- LISTADO ---
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = librosFiltrados,
                    key = { it.isbn }
                ) { bean ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { value ->
                            if (value != SwipeToDismissBoxValue.Settled) {
                                libroEliminar = bean
                                mostrarDialogo = true
                                true
                            } else {
                                false
                            }
                        }
                    )

                    // Resetear el estado de Swipe si se cancela el diálogo
                    LaunchedEffect(dismissState.currentValue) {
                        if (dismissState.currentValue != SwipeToDismissBoxValue.Settled) {
                            dismissActual = dismissState
                        }
                    }

                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            Card(
                                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFD32F2F)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize().padding(end = 20.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.White)
                                }
                            }
                        }
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(2.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = bean.titulo,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), thickness = 0.5.dp)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("ISBN: ${bean.isbn}", style = MaterialTheme.typography.bodySmall)
                                    Text("S/. ${bean.precio}", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                                }
                                Text("Stock disponible: ${bean.stock}", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }

            // --- DIÁLOGO DE CONFIRMACIÓN ---
            if (mostrarDialogo) {
                AlertDialog(
                    onDismissRequest = {
                        scope.launch {
                            dismissActual?.snapTo(SwipeToDismissBoxValue.Settled)
                            mostrarDialogo = false
                        }
                    },
                    title = { Text("Eliminar Libro") },
                    text = { Text("¿Deseas eliminar permanentemente '${libroEliminar?.titulo}'?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                libroEliminar?.let { viewLib.delete(it.isbn) }
                                mostrarDialogo = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) { Text("Eliminar", color = Color.White) }
                    },
                    dismissButton = {
                        OutlinedButton(
                            onClick = {
                                scope.launch {
                                    dismissActual?.snapTo(SwipeToDismissBoxValue.Settled)
                                    mostrarDialogo = false
                                }
                            }
                        ) { Text("Cancelar") }
                    }
                )
            }
        }
    }
}
