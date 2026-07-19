package com.example.appcolegioclass.firebase.repository

import com.example.appcolegioclass.firebase.entidades.Libro
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class LibroRepository {
    // Acceder a la base de datos
    private val db = FirebaseFirestore.getInstance()

    suspend fun registrarLibro(bean: Libro): Result<Unit> {
        try {
            // Crear colletion
            db.collection("libros")
                .document(bean.isbn)
                .set(bean)
                .await()
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun listarLibros(): Result<List<Libro>> {
        return try {
            val documentos = db.collection("libros").get().await()
            val lista = documentos.toObjects(Libro::class.java)
            Result.success(lista)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminarLibro(isbn: String): Result<Unit> {
        return try {
            db.collection("libros").document(isbn).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun buscarLibro(isbn: String): Result<Libro?> {
        return try {
            val doc = db.collection("libros").document(isbn).get().await()
            val libro = doc.toObject(Libro::class.java)
            Result.success(libro)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}