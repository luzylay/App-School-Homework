package com.example.appcolegioclass.firebase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcolegioclass.firebase.entidades.Libro
import com.example.appcolegioclass.firebase.repository.LibroRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LibroViewModel: ViewModel(){
    // Crear objeto del repositorio
    private val repo = LibroRepository()

    // Variable interna
    private val _mensaje = MutableStateFlow<String?>(null)
    // Variable para la UI
    val mensaje: StateFlow<String?> = _mensaje

    private val _libros = MutableStateFlow<List<Libro>>(emptyList())
    val libros: StateFlow<List<Libro>> = _libros

    fun findAll() {
        viewModelScope.launch {
            repo.listarLibros()
                .onSuccess {
                    _libros.value = it
                }
                .onFailure {
                    _mensaje.value = it.message
                }
        }
    }

    fun delete(isbn: String) {
        viewModelScope.launch {
            repo.eliminarLibro(isbn)
                .onSuccess {
                    _mensaje.value = "Libro Eliminado"
                    findAll()
                }
                .onFailure {
                    _mensaje.value = it.message
                }
        }
    }

    fun save(lib : Libro){
        viewModelScope.launch {
            repo.registrarLibro(lib)
                .onSuccess {
                    _mensaje.value = "Libro Registrado"
                    findAll()
                }
                .onFailure {
                    _mensaje.value = it.message
                }
        }
    }
    fun limpiarMensaje(){
        _mensaje.value = null
    }
}
