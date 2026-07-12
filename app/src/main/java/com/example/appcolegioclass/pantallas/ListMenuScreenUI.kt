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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.appcolegioclass.retrofit.RetrofitClient
import com.example.appcolegioclass.retrofit.entidades.Menu
import com.example.appcolegioclass.util.SnackbarManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaMenu(
    // --- FLUJO DE NAVEGACIÓN: Callbacks para cambiar entre pantallas ---
    addMenu: () -> Unit,
    editMenu: (Int) -> Unit,
    verDocentes: () -> Unit,
    verCursos: () -> Unit,
    verAlumnos: () -> Unit,
    verMenus: () -> Unit,
    verLibros: () -> Unit
) {
    // --- FLUJO DE DATOS: Acceso al DAO para interactuar con la tabla de docentes ---
    val scope = rememberCoroutineScope()

    // --- FLUJO DE ESTADO: Estado que mantiene la lista de docentes para la UI ---
    var lista by remember {
        mutableStateOf<List<Menu>>(listOf())
    }

    var mostrarDialogo by remember { mutableStateOf(false) }
    var dismissActual by remember { mutableStateOf<SwipeToDismissBoxState?>(null) }
    var menuActual by remember { mutableStateOf<Menu?>(null) }
    var valorBuscado by remember { mutableStateOf("") }
    var isDeleting by remember { mutableStateOf(false) }

    // --- FLUJO DE DATOS: Polling para sincronización automática (60s) ---
    LaunchedEffect(Unit) {
        while (true) {
            try {
                val response = RetrofitClient.menuApi.listarMenus()
                if (valorBuscado.isEmpty() && response.success && response.data.isJsonArray) {
                    val listType = object : TypeToken<List<Menu>>() {}.type
                    lista = Gson().fromJson(response.data, listType)
                }
            } catch (e: Exception) {
                // Silently fail polling
            }
            delay(60000)
        }
    }

    // --- FLUJO DE DATOS: Efecto de carga inicial y filtrado ---
    LaunchedEffect(valorBuscado) {
        scope.launch {
            try {
                val response = RetrofitClient.menuApi.listarMenus()
                if (response.success && response.data.isJsonArray) {
                    val listType = object : TypeToken<List<Menu>>() {}.type
                    val data: List<Menu> = Gson().fromJson(response.data, listType)
                    lista = if (valorBuscado.isEmpty()) {
                        data
                    } else {
                        data.filter { it.nombre.contains(valorBuscado, ignoreCase = true) }
                    }
                }
            } catch (e: Exception) {
                SnackbarManager.showMessage("Error al conectar con el servidor")
            }
        }
    }

    // --- ESTILO Y ESTRUCTURA: Contenedor principal Scaffold ---
    Scaffold(
        topBar = {
            // --- ESTILO: Barra superior con colores personalizados ---
            TopAppBar(
                title = { Text("Gestión de Menús", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1976D2),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
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
                                fontWeight = if (label == "Menús") FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            // --- FLUJO: Botón para navegar a la pantalla de agregar docente ---
            FloatingActionButton(
                onClick = { addMenu() },
                containerColor = Color(0xFF1976D2),
                contentColor = Color.White
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
                            if (value != SwipeToDismissBoxValue.Settled && !isDeleting) {
                                menuActual = bean
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
                            onClick = { if (!isDeleting) editMenu(bean.codigo) }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    // --- TAMAÑO: Espacio de 10dp entre las líneas de información ---
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                ) {
                                    // --- ESTILO: Resaltado con negrita para el código ---
                                    Text("Código : ${bean.codigo}", fontWeight = FontWeight.Bold)
                                    Text("Nombres : ${bean.nombre}")
                                    Text("Precio : ${bean.precio}")
                                }
                                if (isDeleting && menuActual?.codigo == bean.codigo) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                } else {
                                    AsyncImage(
                                        model = bean.foto.ifEmpty { "https://res.cloudinary.com/dfntftd2h/image/upload/v1731639352/notfound_x7zr8p.png" },
                                        contentDescription = null,
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
                Text("¿Seguro de eliminar el menú?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        val originalList = lista
                        val targetMenu = menuActual
                        mostrarDialogo = false
                        isDeleting = true

                        // --- UX: Optimistic UI - Eliminar visualmente antes de la respuesta ---
                        lista = lista.filter { it.codigo != targetMenu?.codigo }

                        scope.launch {
                            try {
                                targetMenu?.let {
                                    RetrofitClient.menuApi.eliminarMenu(it.codigo, it.version)
                                    SnackbarManager.showMessage("Menú eliminado correctamente")
                                }
                            } catch (e: Exception) {
                                // --- UX: Rollback en caso de error o conflicto (409) ---
                                lista = originalList
                                val errorMsg = if (e.message?.contains("409") == true)
                                    "Error: El menú ha sido modificado por otro usuario"
                                else "Error al eliminar el menú"
                                SnackbarManager.showMessage(errorMsg)
                                dismissActual?.snapTo(SwipeToDismissBoxValue.Settled)
                            } finally {
                                isDeleting = false
                                menuActual = null
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
