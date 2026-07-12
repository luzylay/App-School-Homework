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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appcolegioclass.firebase.entidades.Libro
import com.example.appcolegioclass.firebase.viewmodel.LibroViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.example.appcolegioclass.util.SnackbarManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdicionarLibro(onBack:()->Unit){

    var isbn by remember { mutableStateOf("") }
    var titulo by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    //Objeto de la clase LibroViewModel
    val viewLib: LibroViewModel = viewModel()
    //Recuperar la variable "mensaje" de tipo StateFlow
    val men by viewLib.mensaje.collectAsState()

    //Ejecutar codigo cuando se actualiza la variable "men"
    LaunchedEffect(men){
        //Validar que no sea nulo
        men?.let{
            SnackbarManager.showMessage(it)
            if (it == "Libro Registrado") {
                onBack()
            }
            viewLib.limpiarMensaje()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { Text("Registrar Libro") },
                navigationIcon = {
                    IconButton(
                        onClick = {onBack()}
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ){ espacio->
        Column(
            modifier = Modifier
                .padding(espacio)
                .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = isbn,
                onValueChange = {isbn=it},
                label = {Text("Ingresar isbn")},
                modifier = Modifier
                    .fillMaxWidth()
            )
            OutlinedTextField(
                value = titulo,
                onValueChange = {titulo=it},
                label = {Text("Ingresar titulo")},
                modifier = Modifier
                    .fillMaxWidth()
            )
            OutlinedTextField(
                value = precio,
                onValueChange = {precio=it},
                label = {Text("Ingresar precio")},
                modifier = Modifier
                    .fillMaxWidth()
            )
            OutlinedTextField(
                value = stock,
                onValueChange = {stock=it},
                label = {Text("Ingresar stock")},
                modifier = Modifier
                    .fillMaxWidth()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ){
                Button(
                    onClick = {
                        val p = precio.toDoubleOrNull() ?: 0.0
                        val s = stock.toIntOrNull() ?: 0
                        viewLib.save(Libro(isbn, titulo, p, s))
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(5.dp)
                ){
                    Text("Grabar")
                }
            }

        }

    }


}