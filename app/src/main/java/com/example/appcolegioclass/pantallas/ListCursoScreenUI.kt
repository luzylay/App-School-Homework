package com.example.appcolegioclass.pantallas

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appcolegioclass.local.AppDatabase
import com.example.appcolegioclass.local.entidades.Curso
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaCurso(
    // --- FLUJO DE NAVEGACIÓN: Funciones de callback para cambiar entre pantallas ---
    addCurso: () -> Unit,
    verDocentes: () -> Unit,
    verCursos: () -> Unit,
    verAlumnos: () -> Unit,
    verMenus: () -> Unit,
    // --- FLUJO DE DATOS: Inyección de la base de datos de cursos ---
    db: AppDatabase,
    // --- FLUJO DE NAVEGACIÓN: Callback para navegar a la pantalla de detalles de curso ---
    datosCurso: (Int) -> Unit
) {
    // --- FLUJO DE DATOS: Acceso al DAO para interactuar con la base de datos Room ---
    val dao = db.cursoDao()
    val scope = rememberCoroutineScope()
    
    // --- FLUJO DE ESTADO: Variable reactiva que almacena la lista de cursos cargados ---
    var lista by remember { mutableStateOf(listOf<Curso>()) }

    var mostrarDialogo by remember { mutableStateOf(false) }
    var dismissActual by remember { mutableStateOf<SwipeToDismissBoxState?>(null) }
    var cursoActual by remember { mutableStateOf<Curso?>(null) }

    var valorBuscado by remember { mutableStateOf("") }

    // --- FLUJO DE DATOS: Bloque que carga los datos de la base de datos al iniciar la pantalla ---
    LaunchedEffect(valorBuscado) {
        lista = if (valorBuscado.isEmpty()) {
            dao.listar()
        } else {
            dao.listar().filter {
                it.nombre.contains(valorBuscado, ignoreCase = true)
            }
        }
    }

    // --- ESTILO Y ESTRUCTURA: Contenedor principal Scaffold para organizar la pantalla ---
    Scaffold(
        topBar = {
            // --- ESTILO: Barra superior con personalización de colores ---
            TopAppBar(
                title = { Text("Cursos", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue, // Fondo azul (Estilo)
                    titleContentColor = Color.White // Texto blanco (Estilo)
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFFF5F5F5),
                contentColor = Color(0xFF003366)
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
                                fontWeight = if (label == "Cursos") FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            // --- FLUJO: Botón flotante que dispara la navegación al formulario de registro ---
            FloatingActionButton(onClick = addCurso) {
                // --- ESTILO: Icono visual de "+" ---
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { espacio ->
        Column(
            modifier = Modifier
                // --- TAMAÑO: El padding 'espacio' evita que el contenido quede debajo de las barras ---
                .padding(espacio)
                // --- TAMAÑO: Márgenes externos del contenedor principal (lados y arriba/abajo) ---
                .padding(horizontal = 15.dp, vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = valorBuscado,
                onValueChange = { valorBuscado = it },
                label = { Text("Buscar") },
                // --- TAMAÑO: El campo de búsqueda ocupa todo el ancho ---
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                }
            )
            // --- ESTILO Y ESTRUCTURA: Lista con desplazamiento (scroll) vertical ---
            LazyColumn(
                // --- TAMAÑO: La lista ocupa todo el ancho disponible ---
                modifier = Modifier.fillMaxWidth(),
                // --- TAMAÑO: Margen interno arriba y abajo de la lista ---
                contentPadding = PaddingValues(vertical = 15.dp),
                // --- TAMAÑO: Espacio automático de 10dp entre cada tarjeta ---
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // --- FLUJO DE RENDERIZADO: Itera sobre la lista usando el código como clave para actualizaciones precisas ---
                items(lista, key = { it.codigo }) { bean ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { value ->
                            if (value != SwipeToDismissBoxValue.Settled) {
                                cursoActual = bean
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
                        backgroundContent = {
                            Card(
                                // --- TAMAÑO: El fondo rojo ocupa todo el ancho de la fila ---
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Red
                                )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        // --- TAMAÑO: Margen para alejar el icono del borde derecho ---
                                        .padding(30.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    ) {
                        // --- ESTILO: Diseño de tarjeta para mostrar cada curso ---
                        Card(
                            // --- TAMAÑO: La tarjeta ocupa todo el ancho disponible ---
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { datosCurso(bean.codigo) }
                        ) {
                            Column(
                                // --- TAMAÑO: Espaciado interno entre el borde de la tarjeta y el texto ---
                                modifier = Modifier.padding(10.dp),
                                // --- TAMAÑO: Espacio de 5dp entre las líneas de información ---
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                // --- ESTILO: Resaltado de texto con FontWeight.Bold ---
                                Text("Código : ${bean.codigo}", fontWeight = FontWeight.Bold)
                                Text("Nombre: ${bean.nombre}")
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
                scope.launch {
                    dismissActual?.snapTo(SwipeToDismissBoxValue.Settled)
                }
            },
            title = {
                Text("Confirmación")
            },
            text = {
                Text("¿Seguro de eliminar el curso?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            cursoActual?.let {
                                // 1. Eliminar de la BD
                                dao.eliminar(it)
                                // 2. Obtener lista actualizada y reaplicar filtro
                                val data = dao.listar()
                                lista = if (valorBuscado.isEmpty()) {
                                    data
                                } else {
                                    data.filter { c ->
                                        c.nombre.contains(valorBuscado, ignoreCase = true)
                                    }
                                }
                                // 3. Cerrar diálogo y limpiar referencia
                                mostrarDialogo = false
                                cursoActual = null
                            }
                        }
                    }
                ) {
                    Text("Sí")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        mostrarDialogo = false
                        scope.launch {
                            dismissActual?.snapTo(SwipeToDismissBoxValue.Settled)
                        }
                    }
                ) {
                    Text("No")
                }
            }
        )
    }
}
