package com.example.appcolegioclass.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.appcolegioclass.local.entidades.Curso

@Dao
interface CursoDao {
    @Query("SELECT * FROM cursos")
    suspend fun listar(): List<Curso>

    @Insert
    suspend fun insertar(bean: Curso)

    @Update
    suspend fun actualizar(bean: Curso)

    @Query("SELECT * FROM cursos WHERE codigo = :cod")
    suspend fun buscarPorCodigo(cod: Int): Curso?

    @Delete
    suspend fun eliminar(bean: Curso)

    @Query("SELECT * FROM cursos WHERE nombre LIKE :nombre || '%'")
    suspend fun buscarPorNombre(nombre: String): List<Curso>
}
