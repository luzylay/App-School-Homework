package com.example.appcolegioclass.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.appcolegioclass.local.entidades.Docente

@Dao
interface DocenteDao {
    @Query("SELECT * FROM docentes")
    suspend fun listar(): List<Docente>

    @Insert
    suspend fun registrar(bean: Docente)

    @Query("SELECT * FROM docentes WHERE codigo = :cod")
    suspend fun buscarPorCodigo(cod: Int): Docente?

    @Update
    suspend fun actualizar(bean: Docente)

    @Delete
    suspend fun eliminar(bean: Docente)

    @Query("SELECT * FROM docentes WHERE nombres LIKE :nombre || '%'")
    suspend fun buscarPorNombre(nombre: String): List<Docente>

    @Query("SELECT * FROM docentes WHERE apellidos LIKE :apellido || '%'")
    suspend fun buscarPorApellido(apellido: String): List<Docente>
}
