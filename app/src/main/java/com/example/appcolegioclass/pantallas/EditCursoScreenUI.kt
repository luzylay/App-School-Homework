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
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appcolegioclass.local.AppDatabase
import com.example.appcolegioclass.local.entidades.Curso
import com.example.appcolegioclass.util.SnackbarManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarCurso(
    // --- FLUJO DE NAVEGACIÓN: Callback para retornar a la lista ---
    onBack: () -> Unit,
    // --- FLUJO DE DATOS: Inyección de la base de datos ---
    db: AppDatabase,
    codigo: Int
) {
    // --- FLUJO DE DATOS: Acceso al DAO para operaciones de escritura ---
    val dao = db.cursoDao()

    // --- FLUJO: Ámbito de corrutina para realizar operaciones suspendidas (DB) ---
    val scope = rememberCoroutineScope()

    // --- FLUJO DE ESTADO: Variables reactivas para capturar la entrada del usuario ---
    var nombre by remember { mutableStateOf("") }
    var creditos by remember { mutableStateOf("") }

    // Configuración de opciones para los desplegables
    val ciclos = listOf("I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X")
    var cycleExpanded by remember { mutableStateOf(false) }
    var selectedCiclo by remember { mutableStateOf("") }

    val carreras = listOf("Ingeniería de Sistemas", "Contabilidad", "Administración", "Derecho")
    var majorExpanded by remember { mutableStateOf(false) }
    var selectedCarrera by remember { mutableStateOf("") }

    var datos by remember { mutableStateOf<Curso?>(null) }

    // --- FLUJO DE DATOS: Carga inicial de datos del curso a editar ---
    LaunchedEffect(Unit) {
        scope.launch {
            datos = dao.buscarPorCodigo(codigo)
            datos?.let {
                nombre = it.nombre
                selectedCiclo = it.ciclo
                creditos = it.creditos.toString()
                selectedCarrera = it.carrera
            }
        }
    }

    // --- ESTILO Y ESTRUCTURA: Contenedor principal ---
    Scaffold(
        topBar = {
            // --- ESTILO: Barra superior con colores estándar del proyecto ---
            TopAppBar(
                title = { Text("Actualizar Curso", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue,
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
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del curso") },
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
                    ciclos.forEach { cicloItem ->
                        DropdownMenuItem(
                            text = { Text(cicloItem) },
                            onClick = {
                                selectedCiclo = cicloItem
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
                    carreras.forEach { carreraItem ->
                        DropdownMenuItem(
                            text = { Text(carreraItem) },
                            onClick = {
                                selectedCarrera = carreraItem
                                majorExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- FLUJO DE DATOS: Botón para persistir el curso en la DB ---
            Button(
                onClick = {
                    scope.launch {
                        try {
                            if (nombre.isNotBlank() && selectedCiclo.isNotBlank() && creditos.isNotBlank() && selectedCarrera.isNotBlank()) {
                                dao.actualizar(
                                    Curso(
                                        codigo = datos!!.codigo,
                                        nombre = nombre,
                                        ciclo = selectedCiclo,
                                        creditos = creditos.toDoubleOrNull() ?: 0.0,
                                        carrera = selectedCarrera
                                    )
                                )
                                SnackbarManager.showMessage("Curso actualizado")
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
                shape = RoundedCornerShape(5.dp) // ESTILO: Bordes rectangulares suaves
            ) {
                Text("Actualizar", fontWeight = FontWeight.Bold)
            }
        }
    }
}
