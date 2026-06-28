package com.example.appcolegioclass.pantallas

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.appcolegioclass.local.AppDatabase
import com.example.appcolegioclass.local.entidades.Docente
import com.example.appcolegioclass.util.SnackbarManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaDocente(
    addDocente: () -> Unit,
    verDocentes: () -> Unit,
    verCursos: () -> Unit,
    verAlumnos: () -> Unit,
    verMenus: () -> Unit,
    db: AppDatabase,
    datosDocente: (Int) -> Unit,
) {
    val dao = db.docenteDao()
    val scope = rememberCoroutineScope()

    var lista by remember { mutableStateOf<List<Docente>>(listOf()) }
    var mostrarDialogo by remember { mutableStateOf(false) }
    var dismissActual by remember { mutableStateOf<SwipeToDismissBoxState?>(null) }
    var docenteActual by remember { mutableStateOf<Docente?>(null) }
    var valorBuscado by remember { mutableStateOf("") }
    
    // UX: Estado para deshabilitar botones durante operaciones
    var estaOperando by remember { mutableStateOf(false) }

    fun cargarDocentes() {
        scope.launch {
            val data = dao.listar()
            lista = if (valorBuscado.isEmpty()) {
                data
            } else {
                data.filter {
                    it.nombres.contains(valorBuscado, ignoreCase = true) ||
                    it.apellidos.contains(valorBuscado, ignoreCase = true)
                }
            }
        }
    }

    // SINCRONIZACIÓN: Polling periódico (cada 60s) para asegurar consistencia
    LaunchedEffect(Unit) {
        while(true) {
            cargarDocentes()
            delay(60000)
        }
    }

    LaunchedEffect(valorBuscado) {
        cargarDocentes()
    }

    // --- ESTILO Y ESTRUCTURA: Contenedor principal Scaffold ---
    Scaffold(
        topBar = {
            // --- ESTILO: Barra superior con colores personalizados ---
            TopAppBar(
                title = { Text("Docente", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue, // Color de fondo
                    titleContentColor = Color.White // Color del texto
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
                                fontWeight = if (label == "Docentes") FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            // --- FLUJO: Botón para navegar a la pantalla de agregar docente ---
            FloatingActionButton(
                onClick = { addDocente() }
            ) {
                // --- ESTILO: Icono de "+" ---
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    ) { espacio ->
        Column(
            modifier = Modifier
                // --- TAMAÑO: El padding 'espacio' evita que el contenido quede debajo de las barras ---
                .padding(espacio)
                // --- TAMAÑO: Márgenes externos del contenedor principal ---
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
            // --- ESTILO Y ESTRUCTURA: Lista vertical optimizada ---
            LazyColumn(
                // --- TAMAÑO: La lista ocupa todo el ancho disponible ---
                modifier = Modifier.fillMaxWidth(),
                // --- TAMAÑO: Margen interno arriba y abajo de la lista ---
                contentPadding = PaddingValues(vertical = 15.dp),
                // --- TAMAÑO: Espaciado automático de 10dp entre cada tarjeta ---
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // --- FLUJO DE RENDERIZADO: Genera una tarjeta por cada docente con clave única ---
                items(lista, key = { it.codigo }) { bean ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { value ->
                            if (value != SwipeToDismissBoxValue.Settled) {
                                docenteActual = bean
                                mostrarDialogo = true
                                true
                            } else {
                                false
                            }
                        }
                    )

                    // Efecto para capturar el estado del dismiss cuando cambia
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
                                        // --- TAMAÑO: Margen para alejar el icono del borde ---
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
                        // --- ESTILO: Diseño de tarjeta para cada registro ---
                        Card(
                            // --- TAMAÑO: La tarjeta de datos ocupa todo el ancho ---
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { datosDocente(bean.codigo) }
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = bean.foto.ifEmpty { "https://res.cloudinary.com/dfntftd2h/image/upload/v1731639352/notfound_x7zr8p.png" },
                                    contentDescription = null,
                                    modifier = Modifier.size(80.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Column(
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(5.dp),
                                ) {
                                    // --- ESTILO: Resaltado con negrita para el código ---
                                    Text("Código : ${bean.codigo}", fontWeight = FontWeight.Bold)
                                    Text("Nombres : ${bean.nombres}")
                                    Text("Apellidos : ${bean.apellidos}")
                                }
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
                Text("¿Seguro de eliminar el docente?")
            },
            confirmButton = {
                Button(
                    enabled = !estaOperando, // UX: Evita doble clic
                    onClick = {
                        scope.launch {
                            docenteActual?.let { seleccionado ->
                                estaOperando = true
                                val listaOriginal = lista // Respaldo para Rollback
                                
                                try {
                                    // Optimistic UI: Borrado visual inmediato
                                    lista = lista.filter { it.codigo != seleccionado.codigo }
                                    SnackbarManager.showMessage("Borrando docente...")
                                    
                                    dao.eliminar(seleccionado)
                                    
                                    SnackbarManager.showMessage("Docente eliminado")
                                    mostrarDialogo = false
                                } catch (e: Exception) {
                                    // Rollback en caso de error local
                                    lista = listaOriginal
                                    SnackbarManager.showMessage("Error al eliminar docente")
                                } finally {
                                    estaOperando = false
                                    docenteActual = null
                                    cargarDocentes() // Sincronización final
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
