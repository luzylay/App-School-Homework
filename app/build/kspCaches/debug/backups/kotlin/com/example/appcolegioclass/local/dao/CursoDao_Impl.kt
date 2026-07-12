package com.example.appcolegioclass.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.appcolegioclass.local.entidades.Curso
import javax.`annotation`.processing.Generated
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class CursoDao_Impl(
  __db: RoomDatabase,
) : CursoDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfCurso: EntityInsertAdapter<Curso>

  private val __deleteAdapterOfCurso: EntityDeleteOrUpdateAdapter<Curso>

  private val __updateAdapterOfCurso: EntityDeleteOrUpdateAdapter<Curso>
  init {
    this.__db = __db
    this.__insertAdapterOfCurso = object : EntityInsertAdapter<Curso>() {
      protected override fun createQuery(): String =
          "INSERT OR ABORT INTO `cursos` (`codigo`,`nombre`,`ciclo`,`creditos`,`carrera`,`version`) VALUES (nullif(?, 0),?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Curso) {
        statement.bindLong(1, entity.codigo.toLong())
        statement.bindText(2, entity.nombre)
        statement.bindText(3, entity.ciclo)
        statement.bindDouble(4, entity.creditos)
        statement.bindText(5, entity.carrera)
        statement.bindLong(6, entity.version.toLong())
      }
    }
    this.__deleteAdapterOfCurso = object : EntityDeleteOrUpdateAdapter<Curso>() {
      protected override fun createQuery(): String = "DELETE FROM `cursos` WHERE `codigo` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Curso) {
        statement.bindLong(1, entity.codigo.toLong())
      }
    }
    this.__updateAdapterOfCurso = object : EntityDeleteOrUpdateAdapter<Curso>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `cursos` SET `codigo` = ?,`nombre` = ?,`ciclo` = ?,`creditos` = ?,`carrera` = ?,`version` = ? WHERE `codigo` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Curso) {
        statement.bindLong(1, entity.codigo.toLong())
        statement.bindText(2, entity.nombre)
        statement.bindText(3, entity.ciclo)
        statement.bindDouble(4, entity.creditos)
        statement.bindText(5, entity.carrera)
        statement.bindLong(6, entity.version.toLong())
        statement.bindLong(7, entity.codigo.toLong())
      }
    }
  }

  public override suspend fun insertar(bean: Curso): Unit = performSuspending(__db, false, true) {
      _connection ->
    __insertAdapterOfCurso.insert(_connection, bean)
  }

  public override suspend fun eliminar(bean: Curso): Unit = performSuspending(__db, false, true) {
      _connection ->
    __deleteAdapterOfCurso.handle(_connection, bean)
  }

  public override suspend fun actualizar(bean: Curso): Unit = performSuspending(__db, false, true) {
      _connection ->
    __updateAdapterOfCurso.handle(_connection, bean)
  }

  public override suspend fun listar(): List<Curso> {
    val _sql: String = "SELECT * FROM cursos"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfCodigo: Int = getColumnIndexOrThrow(_stmt, "codigo")
        val _columnIndexOfNombre: Int = getColumnIndexOrThrow(_stmt, "nombre")
        val _columnIndexOfCiclo: Int = getColumnIndexOrThrow(_stmt, "ciclo")
        val _columnIndexOfCreditos: Int = getColumnIndexOrThrow(_stmt, "creditos")
        val _columnIndexOfCarrera: Int = getColumnIndexOrThrow(_stmt, "carrera")
        val _columnIndexOfVersion: Int = getColumnIndexOrThrow(_stmt, "version")
        val _result: MutableList<Curso> = mutableListOf()
        while (_stmt.step()) {
          val _item: Curso
          val _tmpCodigo: Int
          _tmpCodigo = _stmt.getLong(_columnIndexOfCodigo).toInt()
          val _tmpNombre: String
          _tmpNombre = _stmt.getText(_columnIndexOfNombre)
          val _tmpCiclo: String
          _tmpCiclo = _stmt.getText(_columnIndexOfCiclo)
          val _tmpCreditos: Double
          _tmpCreditos = _stmt.getDouble(_columnIndexOfCreditos)
          val _tmpCarrera: String
          _tmpCarrera = _stmt.getText(_columnIndexOfCarrera)
          val _tmpVersion: Int
          _tmpVersion = _stmt.getLong(_columnIndexOfVersion).toInt()
          _item = Curso(_tmpCodigo,_tmpNombre,_tmpCiclo,_tmpCreditos,_tmpCarrera,_tmpVersion)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun buscarPorCodigo(cod: Int): Curso? {
    val _sql: String = "SELECT * FROM cursos WHERE codigo = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, cod.toLong())
        val _columnIndexOfCodigo: Int = getColumnIndexOrThrow(_stmt, "codigo")
        val _columnIndexOfNombre: Int = getColumnIndexOrThrow(_stmt, "nombre")
        val _columnIndexOfCiclo: Int = getColumnIndexOrThrow(_stmt, "ciclo")
        val _columnIndexOfCreditos: Int = getColumnIndexOrThrow(_stmt, "creditos")
        val _columnIndexOfCarrera: Int = getColumnIndexOrThrow(_stmt, "carrera")
        val _columnIndexOfVersion: Int = getColumnIndexOrThrow(_stmt, "version")
        val _result: Curso?
        if (_stmt.step()) {
          val _tmpCodigo: Int
          _tmpCodigo = _stmt.getLong(_columnIndexOfCodigo).toInt()
          val _tmpNombre: String
          _tmpNombre = _stmt.getText(_columnIndexOfNombre)
          val _tmpCiclo: String
          _tmpCiclo = _stmt.getText(_columnIndexOfCiclo)
          val _tmpCreditos: Double
          _tmpCreditos = _stmt.getDouble(_columnIndexOfCreditos)
          val _tmpCarrera: String
          _tmpCarrera = _stmt.getText(_columnIndexOfCarrera)
          val _tmpVersion: Int
          _tmpVersion = _stmt.getLong(_columnIndexOfVersion).toInt()
          _result = Curso(_tmpCodigo,_tmpNombre,_tmpCiclo,_tmpCreditos,_tmpCarrera,_tmpVersion)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun buscarPorNombre(nombre: String): List<Curso> {
    val _sql: String = "SELECT * FROM cursos WHERE nombre LIKE ? || '%'"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, nombre)
        val _columnIndexOfCodigo: Int = getColumnIndexOrThrow(_stmt, "codigo")
        val _columnIndexOfNombre: Int = getColumnIndexOrThrow(_stmt, "nombre")
        val _columnIndexOfCiclo: Int = getColumnIndexOrThrow(_stmt, "ciclo")
        val _columnIndexOfCreditos: Int = getColumnIndexOrThrow(_stmt, "creditos")
        val _columnIndexOfCarrera: Int = getColumnIndexOrThrow(_stmt, "carrera")
        val _columnIndexOfVersion: Int = getColumnIndexOrThrow(_stmt, "version")
        val _result: MutableList<Curso> = mutableListOf()
        while (_stmt.step()) {
          val _item: Curso
          val _tmpCodigo: Int
          _tmpCodigo = _stmt.getLong(_columnIndexOfCodigo).toInt()
          val _tmpNombre: String
          _tmpNombre = _stmt.getText(_columnIndexOfNombre)
          val _tmpCiclo: String
          _tmpCiclo = _stmt.getText(_columnIndexOfCiclo)
          val _tmpCreditos: Double
          _tmpCreditos = _stmt.getDouble(_columnIndexOfCreditos)
          val _tmpCarrera: String
          _tmpCarrera = _stmt.getText(_columnIndexOfCarrera)
          val _tmpVersion: Int
          _tmpVersion = _stmt.getLong(_columnIndexOfVersion).toInt()
          _item = Curso(_tmpCodigo,_tmpNombre,_tmpCiclo,_tmpCreditos,_tmpCarrera,_tmpVersion)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
