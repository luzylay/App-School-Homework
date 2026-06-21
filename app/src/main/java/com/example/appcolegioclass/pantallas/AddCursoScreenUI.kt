package com.example.appcolegioclass.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
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
import com.example.appcolegioclass.local.AppDatabase
import com.example.appcolegioclass.local.entidades.Curso
import com.example.appcolegioclass.util.SnackbarManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdicionarCurso(
    // --- FLUJO DE NAVEGACIÓN: Callback para regresar a la pantalla anterior ---
    onBack: () -> Unit, 
    // --- FLUJO DE DATOS: Inyección de la base de datos para persistencia ---
    db: AppDatabase
) {
    // --- FLUJO DE DATOS: Scope para ejecutar corrutinas (operaciones en segundo plano) ---
    val scope = rememberCoroutineScope()

    // --- FLUJO DE ESTADO: Variables reactivas que capturan la entrada del usuario ---
    var nombre by remember { mutableStateOf("") }
    var creditos by remember { mutableStateOf("") }
    
    // Configuración de opciones para los desplegables
    val ciclos = listOf("I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X")
    var cycleExpanded by remember { mutableStateOf(false) }
    var selectedCiclo by remember { mutableStateOf("") }

    val carreras = listOf("Ingeniería de Sistemas", "Contabilidad", "Administración", "Derecho")
    var majorExpanded by remember { mutableStateOf(false) }
    var selectedCarrera by remember { mutableStateOf("") }

    // --- ESTILO Y ESTRUCTURA: Layout base de la pantalla ---
    Scaffold(
        topBar = {
            // --- ESTILO: Barra superior con título y botón de retroceso ---
            TopAppBar(
                title = { Text("Registrar Curso", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { padding ->
        // --- ESTILO: Columna contenedora con espaciado vertical ---
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(15.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // --- ESTILO Y FLUJO: Campo de texto para el nombre ---
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Curso") },
                modifier = Modifier.fillMaxWidth()
            )

            // --- ESTILO Y FLUJO: Menú desplegable (Dropdown) para el Ciclo ---
            ExposedDropdownMenuBox(
                expanded = cycleExpanded,
                onExpandedChange = { cycleExpanded = !cycleExpanded }
            ) {
                OutlinedTextField(
                    value = selectedCiclo,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("[Seleccione Ciclo]") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(cycleExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded = cycleExpanded,
                    onDismissRequest = { cycleExpanded = false }
                ) {
                    ciclos.forEach { ciclo ->
                        DropdownMenuItem(
                            text = { Text(ciclo) },
                            onClick = {
                                selectedCiclo = ciclo
                                cycleExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = creditos,
                onValueChange = { creditos = it },
                label = { Text("Créditos") },
                modifier = Modifier.fillMaxWidth()
            )

            // --- ESTILO Y FLUJO: Menú desplegable (Dropdown) para la Carrera ---
            ExposedDropdownMenuBox(
                expanded = majorExpanded,
                onExpandedChange = { majorExpanded = !majorExpanded }
            ) {
                OutlinedTextField(
                    value = selectedCarrera,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("[Seleccione Carrera]") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(majorExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded = majorExpanded,
                    onDismissRequest = { majorExpanded = false }
                ) {
                    carreras.forEach { carrera ->
                        DropdownMenuItem(
                            text = { Text(carrera) },
                            onClick = {
                                selectedCarrera = carrera
                                majorExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- FLUJO DE DATOS: Lógica de guardado ---
            Button(
                onClick = {
                    // Validación de campos obligatorios
                    if (nombre.isNotBlank() && selectedCiclo.isNotBlank() && creditos.isNotBlank() && selectedCarrera.isNotBlank()) {
                        scope.launch {
                            // Creación del objeto entidad
                            val nuevoCurso = Curso(
                                nombre = nombre,
                                ciclo = selectedCiclo,
                                creditos = creditos.toDoubleOrNull() ?: 0.0,
                                carrera = selectedCarrera
                            )
                            // Inserción en base de datos Room
                            db.cursoDao().insertar(nuevoCurso)
                            // Notificación al usuario
                            SnackbarManager.showMessage("Curso registrado exitosamente")
                            // Regresar a la lista
                            onBack()
                        }
                    } else {
                        SnackbarManager.showMessage("Por favor complete todos los campos")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(5.dp) // ESTILO: Bordes menos redondeados
            ) {
                Text("Grabar", fontWeight = FontWeight.Bold)
            }
        }
    }
}
