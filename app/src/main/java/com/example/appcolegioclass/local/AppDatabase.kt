package com.example.appcolegioclass.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appcolegioclass.local.dao.CursoDao
import com.example.appcolegioclass.local.dao.DocenteDao
import com.example.appcolegioclass.local.entidades.Curso
import com.example.appcolegioclass.local.entidades.Docente

@Database(entities = [Docente::class, Curso::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun docenteDao(): DocenteDao
    abstract fun cursoDao(): CursoDao
}
