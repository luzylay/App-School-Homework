package com.example.appcolegioclass.navegacion

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import androidx.room.Room
import com.example.appcolegioclass.local.AppDatabase
import com.example.appcolegioclass.pantallas.EditarCurso
import com.example.appcolegioclass.pantallas.EditarDocente
import com.example.appcolegioclass.pantallas.ListaCurso
import com.example.appcolegioclass.pantallas.AdicionarLibro
import com.example.appcolegioclass.pantallas.PantallaLibros
import com.example.appcolegioclass.util.SnackbarManager
import com.example.appcolegioclass.pantallas.AdicionarDocente
import com.example.appcolegioclass.pantallas.ListaDocente
import com.example.appcolegioclass.pantallas.AdicionarCurso
import com.example.appcolegioclass.pantallas.AdicionarMenu
import com.example.appcolegioclass.pantallas.ListaMenu
import com.example.appcolegioclass.pantallas.EditarMenu
import com.example.appcolegioclass.pantallas.ListaAlumno
import com.example.appcolegioclass.pantallas.AdicionarAlumno
import com.example.appcolegioclass.pantallas.EditarAlumno
import com.example.appcolegioclass.pantallas.EditarLibro

// --- FLUJO DE NAVEGACIÓN: Definición de las rutas (destinos) de la aplicación ---
data object ListDocente
data object AddDocente
data object ListCurso
data object AddCurso

data class EditDocente(val cod: Int)
data class EditCurso(val cod: Int)
data class EditMenu(val cod: Int)
data class EditAlumno(val cod: Int)

data object ListMenu
data object AddMenu

data object ListAlumno
data object AddAlumno

data object ListLibro
data object AddLibro
data class EditLibro(val isbn: String)

@Composable
fun Navegar() {

    // --- FLUJO DE NAVEGACIÓN: Estado que controla el historial de pantallas (Backstack) ---
    val backStack = remember { mutableStateListOf<Any>(ListDocente) }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // --- FLUJO DE DATOS: Inicialización del gestor de notificaciones (Snackbars) ---
    LaunchedEffect(Unit) {
        SnackbarManager.init(snackbarHostState, scope)
    }

    // --- FLUJO DE DATOS: Configuración de la Base de Datos Única ---
    val db = remember {
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "colegio.db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    // --- ESTILO Y ESTRUCTURA: Contenedor principal para manejar el layout ---
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) } // Lugar donde aparecerán los mensajes
    ) { padding ->
        // --- FLUJO DE NAVEGACIÓN: Componente que renderiza la pantalla actual según el backStack ---
        NavDisplay(
            modifier = Modifier.padding(padding),
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() }, // Maneja el botón "atrás"
            entryProvider = { key ->
                when (key) {
                    // FLUJO: Pantalla de Lista de Docentes
                    is ListDocente -> NavEntry(key) {
                        ListaDocente(
                            addDocente = { backStack.add(AddDocente) },
                            verDocentes = {
                                backStack.clear()
                                backStack.add(ListDocente)
                            },
                            verCursos = {
                                backStack.clear()
                                backStack.add(ListCurso)
                            },
                            verAlumnos = {
                                backStack.clear()
                                backStack.add(ListAlumno)
                            },
                            verMenus = {
                                backStack.clear()
                                backStack.add(ListMenu)
                            },
                            verLibros = {
                                backStack.clear()
                                backStack.add(ListLibro)
                            },
                            db = db,
                            datosDocente = { backStack.add(EditDocente(it)) }
                        )
                    }

                    // FLUJO: Pantalla de Registro de Docente
                    is AddDocente -> NavEntry(key) {
                        AdicionarDocente(onBack = { backStack.removeLastOrNull() }, db)
                    }

                    // FLUJO: Pantalla de Edición de Docente
                    is EditDocente -> NavEntry(key) {
                        EditarDocente(onBack = { backStack.removeLastOrNull() }, db,
                            codigo = key.cod)
                    }

                    // FLUJO: Pantalla de Registro de Docente
                    is ListMenu -> NavEntry(key) {
                        ListaMenu(
                            addMenu = { backStack.add(AddMenu) },
                            editMenu = { backStack.add(EditMenu(it)) },
                            verDocentes = {
                                backStack.clear()
                                backStack.add(ListDocente)
                            },
                            verCursos = {
                                backStack.clear()
                                backStack.add(ListCurso)
                            },
                            verAlumnos = {
                                backStack.clear()
                                backStack.add(ListAlumno)
                            },
                            verMenus = {
                                backStack.clear()
                                backStack.add(ListMenu)
                            },
                            verLibros = {
                                backStack.clear()
                                backStack.add(ListLibro)
                            }
                        )
                    }

                    is AddMenu -> NavEntry(key) {
                        AdicionarMenu(onBack = { backStack.removeLastOrNull() })
                    }

                    is EditMenu -> NavEntry(key) {
                        EditarMenu(onBack = { backStack.removeLastOrNull() }, codigo = key.cod)
                    }

                    is ListAlumno -> NavEntry(key) {
                        ListaAlumno(
                            addAlumno = { backStack.add(AddAlumno) },
                            datosAlumno = { backStack.add(EditAlumno(it)) },
                            verDocentes = {
                                backStack.clear()
                                backStack.add(ListDocente)
                            },
                            verCursos = {
                                backStack.clear()
                                backStack.add(ListCurso)
                            },
                            verAlumnos = {
                                backStack.clear()
                                backStack.add(ListAlumno)
                            },
                            verMenus = {
                                backStack.clear()
                                backStack.add(ListMenu)
                            },
                            verLibros = {
                                backStack.clear()
                                backStack.add(ListLibro)
                            }
                        )
                    }

                    is AddAlumno -> NavEntry(key) {
                        AdicionarAlumno(onBack = { backStack.removeLastOrNull() })
                    }

                    is EditAlumno -> NavEntry(key) {
                        EditarAlumno(onBack = { backStack.removeLastOrNull() }, codigo = key.cod)
                    }



                    // FLUJO: Pantalla de Lista de Cursos
                    is ListCurso -> NavEntry(key) {
                        ListaCurso(
                            addCurso = { backStack.add(AddCurso) },
                            verDocentes = {
                                backStack.clear()
                                backStack.add(ListDocente)
                            },
                            verCursos = {
                                backStack.clear()
                                backStack.add(ListCurso)
                            },
                            verAlumnos = {
                                backStack.clear()
                                backStack.add(ListAlumno)
                            },
                            verMenus = {
                                backStack.clear()
                                backStack.add(ListMenu)
                            },
                            verLibros = {
                                backStack.clear()
                                backStack.add(ListLibro)
                            },
                            db = db,
                            datosCurso = { backStack.add(EditCurso(it)) }
                        )
                    }

                    // FLUJO: Pantalla de Edición de Curso
                    is EditCurso -> NavEntry(key) {
                        EditarCurso(onBack = { backStack.removeLastOrNull() }, db,
                            codigo = key.cod)
                    }

                    // FLUJO: Pantalla de Registro de Curso
                    is AddCurso -> NavEntry(key) {
                        AdicionarCurso(onBack = { backStack.removeLastOrNull() }, db)
                    }

                    is ListLibro -> NavEntry(key) {
                        PantallaLibros(
                            addLibro = { backStack.add(AddLibro) },
                            editLibro = { backStack.add(EditLibro(it)) },
                            verDocentes = {
                                backStack.clear()
                                backStack.add(ListDocente)
                            },
                            verCursos = {
                                backStack.clear()
                                backStack.add(ListCurso)
                            },
                            verAlumnos = {
                                backStack.clear()
                                backStack.add(ListAlumno)
                            },
                            verMenus = {
                                backStack.clear()
                                backStack.add(ListMenu)
                            },
                            verLibros = {
                                backStack.clear()
                                backStack.add(ListLibro)
                            }
                        )
                    }

                    is AddLibro -> NavEntry(key) {
                        AdicionarLibro(onBack = { backStack.removeLastOrNull() })
                    }

                    is EditLibro -> NavEntry(key) {
                        EditarLibro(onBack = { backStack.removeLastOrNull() }, isbn = key.isbn)
                    }

                    else -> NavEntry(Unit) { Text("Ruta desconocida") }
                }
            }
        )
    }
}
