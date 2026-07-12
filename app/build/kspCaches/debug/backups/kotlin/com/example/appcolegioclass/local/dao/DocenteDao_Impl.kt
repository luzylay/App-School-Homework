package com.example.appcolegioclass.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.appcolegioclass.local.entidades.Docente
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
public class DocenteDao_Impl(
  __db: RoomDatabase,
) : DocenteDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfDocente: EntityInsertAdapter<Docente>

  private val __deleteAdapterOfDocente: EntityDeleteOrUpdateAdapter<Docente>

  private val __updateAdapterOfDocente: EntityDeleteOrUpdateAdapter<Docente>
  init {
    this.__db = __db
    this.__insertAdapterOfDocente = object : EntityInsertAdapter<Docente>() {
      protected override fun createQuery(): String =
          "INSERT OR ABORT INTO `docentes` (`codigo`,`nombres`,`apellidos`,`sexo`,`sueldo`,`hijos`,`foto`,`version`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Docente) {
        statement.bindLong(1, entity.codigo.toLong())
        statement.bindText(2, entity.nombres)
        statement.bindText(3, entity.apellidos)
        statement.bindText(4, entity.sexo)
        statement.bindDouble(5, entity.sueldo)
        statement.bindLong(6, entity.hijos.toLong())
        statement.bindText(7, entity.foto)
        statement.bindLong(8, entity.version.toLong())
      }
    }
    this.__deleteAdapterOfDocente = object : EntityDeleteOrUpdateAdapter<Docente>() {
      protected override fun createQuery(): String = "DELETE FROM `docentes` WHERE `codigo` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Docente) {
        statement.bindLong(1, entity.codigo.toLong())
      }
    }
    this.__updateAdapterOfDocente = object : EntityDeleteOrUpdateAdapter<Docente>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `docentes` SET `codigo` = ?,`nombres` = ?,`apellidos` = ?,`sexo` = ?,`sueldo` = ?,`hijos` = ?,`foto` = ?,`version` = ? WHERE `codigo` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Docente) {
        statement.bindLong(1, entity.codigo.toLong())
        statement.bindText(2, entity.nombres)
        statement.bindText(3, entity.apellidos)
        statement.bindText(4, entity.sexo)
        statement.bindDouble(5, entity.sueldo)
        statement.bindLong(6, entity.hijos.toLong())
        statement.bindText(7, entity.foto)
        statement.bindLong(8, entity.version.toLong())
        statement.bindLong(9, entity.codigo.toLong())
      }
    }
  }

  public override suspend fun registrar(bean: Docente): Unit = performSuspending(__db, false, true)
      { _connection ->
    __insertAdapterOfDocente.insert(_connection, bean)
  }

  public override suspend fun eliminar(bean: Docente): Unit = performSuspending(__db, false, true) {
      _connection ->
    __deleteAdapterOfDocente.handle(_connection, bean)
  }

  public override suspend fun actualizar(bean: Docente): Unit = performSuspending(__db, false, true)
      { _connection ->
    __updateAdapterOfDocente.handle(_connection, bean)
  }

  public override suspend fun listar(): List<Docente> {
    val _sql: String = "SELECT * FROM docentes"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfCodigo: Int = getColumnIndexOrThrow(_stmt, "codigo")
        val _columnIndexOfNombres: Int = getColumnIndexOrThrow(_stmt, "nombres")
        val _columnIndexOfApellidos: Int = getColumnIndexOrThrow(_stmt, "apellidos")
        val _columnIndexOfSexo: Int = getColumnIndexOrThrow(_stmt, "sexo")
        val _columnIndexOfSueldo: Int = getColumnIndexOrThrow(_stmt, "sueldo")
        val _columnIndexOfHijos: Int = getColumnIndexOrThrow(_stmt, "hijos")
        val _columnIndexOfFoto: Int = getColumnIndexOrThrow(_stmt, "foto")
        val _columnIndexOfVersion: Int = getColumnIndexOrThrow(_stmt, "version")
        val _result: MutableList<Docente> = mutableListOf()
        while (_stmt.step()) {
          val _item: Docente
          val _tmpCodigo: Int
          _tmpCodigo = _stmt.getLong(_columnIndexOfCodigo).toInt()
          val _tmpNombres: String
          _tmpNombres = _stmt.getText(_columnIndexOfNombres)
          val _tmpApellidos: String
          _tmpApellidos = _stmt.getText(_columnIndexOfApellidos)
          val _tmpSexo: String
          _tmpSexo = _stmt.getText(_columnIndexOfSexo)
          val _tmpSueldo: Double
          _tmpSueldo = _stmt.getDouble(_columnIndexOfSueldo)
          val _tmpHijos: Int
          _tmpHijos = _stmt.getLong(_columnIndexOfHijos).toInt()
          val _tmpFoto: String
          _tmpFoto = _stmt.getText(_columnIndexOfFoto)
          val _tmpVersion: Int
          _tmpVersion = _stmt.getLong(_columnIndexOfVersion).toInt()
          _item =
              Docente(_tmpCodigo,_tmpNombres,_tmpApellidos,_tmpSexo,_tmpSueldo,_tmpHijos,_tmpFoto,_tmpVersion)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun buscarPorCodigo(cod: Int): Docente? {
    val _sql: String = "SELECT * FROM docentes WHERE codigo = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, cod.toLong())
        val _columnIndexOfCodigo: Int = getColumnIndexOrThrow(_stmt, "codigo")
        val _columnIndexOfNombres: Int = getColumnIndexOrThrow(_stmt, "nombres")
        val _columnIndexOfApellidos: Int = getColumnIndexOrThrow(_stmt, "apellidos")
        val _columnIndexOfSexo: Int = getColumnIndexOrThrow(_stmt, "sexo")
        val _columnIndexOfSueldo: Int = getColumnIndexOrThrow(_stmt, "sueldo")
        val _columnIndexOfHijos: Int = getColumnIndexOrThrow(_stmt, "hijos")
        val _columnIndexOfFoto: Int = getColumnIndexOrThrow(_stmt, "foto")
        val _columnIndexOfVersion: Int = getColumnIndexOrThrow(_stmt, "version")
        val _result: Docente?
        if (_stmt.step()) {
          val _tmpCodigo: Int
          _tmpCodigo = _stmt.getLong(_columnIndexOfCodigo).toInt()
          val _tmpNombres: String
          _tmpNombres = _stmt.getText(_columnIndexOfNombres)
          val _tmpApellidos: String
          _tmpApellidos = _stmt.getText(_columnIndexOfApellidos)
          val _tmpSexo: String
          _tmpSexo = _stmt.getText(_columnIndexOfSexo)
          val _tmpSueldo: Double
          _tmpSueldo = _stmt.getDouble(_columnIndexOfSueldo)
          val _tmpHijos: Int
          _tmpHijos = _stmt.getLong(_columnIndexOfHijos).toInt()
          val _tmpFoto: String
          _tmpFoto = _stmt.getText(_columnIndexOfFoto)
          val _tmpVersion: Int
          _tmpVersion = _stmt.getLong(_columnIndexOfVersion).toInt()
          _result =
              Docente(_tmpCodigo,_tmpNombres,_tmpApellidos,_tmpSexo,_tmpSueldo,_tmpHijos,_tmpFoto,_tmpVersion)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun buscarPorNombre(nombre: String): List<Docente> {
    val _sql: String = "SELECT * FROM docentes WHERE nombres LIKE ? || '%'"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, nombre)
        val _columnIndexOfCodigo: Int = getColumnIndexOrThrow(_stmt, "codigo")
        val _columnIndexOfNombres: Int = getColumnIndexOrThrow(_stmt, "nombres")
        val _columnIndexOfApellidos: Int = getColumnIndexOrThrow(_stmt, "apellidos")
        val _columnIndexOfSexo: Int = getColumnIndexOrThrow(_stmt, "sexo")
        val _columnIndexOfSueldo: Int = getColumnIndexOrThrow(_stmt, "sueldo")
        val _columnIndexOfHijos: Int = getColumnIndexOrThrow(_stmt, "hijos")
        val _columnIndexOfFoto: Int = getColumnIndexOrThrow(_stmt, "foto")
        val _columnIndexOfVersion: Int = getColumnIndexOrThrow(_stmt, "version")
        val _result: MutableList<Docente> = mutableListOf()
        while (_stmt.step()) {
          val _item: Docente
          val _tmpCodigo: Int
          _tmpCodigo = _stmt.getLong(_columnIndexOfCodigo).toInt()
          val _tmpNombres: String
          _tmpNombres = _stmt.getText(_columnIndexOfNombres)
          val _tmpApellidos: String
          _tmpApellidos = _stmt.getText(_columnIndexOfApellidos)
          val _tmpSexo: String
          _tmpSexo = _stmt.getText(_columnIndexOfSexo)
          val _tmpSueldo: Double
          _tmpSueldo = _stmt.getDouble(_columnIndexOfSueldo)
          val _tmpHijos: Int
          _tmpHijos = _stmt.getLong(_columnIndexOfHijos).toInt()
          val _tmpFoto: String
          _tmpFoto = _stmt.getText(_columnIndexOfFoto)
          val _tmpVersion: Int
          _tmpVersion = _stmt.getLong(_columnIndexOfVersion).toInt()
          _item =
              Docente(_tmpCodigo,_tmpNombres,_tmpApellidos,_tmpSexo,_tmpSueldo,_tmpHijos,_tmpFoto,_tmpVersion)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun buscarPorApellido(apellido: String): List<Docente> {
    val _sql: String = "SELECT * FROM docentes WHERE apellidos LIKE ? || '%'"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, apellido)
        val _columnIndexOfCodigo: Int = getColumnIndexOrThrow(_stmt, "codigo")
        val _columnIndexOfNombres: Int = getColumnIndexOrThrow(_stmt, "nombres")
        val _columnIndexOfApellidos: Int = getColumnIndexOrThrow(_stmt, "apellidos")
        val _columnIndexOfSexo: Int = getColumnIndexOrThrow(_stmt, "sexo")
        val _columnIndexOfSueldo: Int = getColumnIndexOrThrow(_stmt, "sueldo")
        val _columnIndexOfHijos: Int = getColumnIndexOrThrow(_stmt, "hijos")
        val _columnIndexOfFoto: Int = getColumnIndexOrThrow(_stmt, "foto")
        val _columnIndexOfVersion: Int = getColumnIndexOrThrow(_stmt, "version")
        val _result: MutableList<Docente> = mutableListOf()
        while (_stmt.step()) {
          val _item: Docente
          val _tmpCodigo: Int
          _tmpCodigo = _stmt.getLong(_columnIndexOfCodigo).toInt()
          val _tmpNombres: String
          _tmpNombres = _stmt.getText(_columnIndexOfNombres)
          val _tmpApellidos: String
          _tmpApellidos = _stmt.getText(_columnIndexOfApellidos)
          val _tmpSexo: String
          _tmpSexo = _stmt.getText(_columnIndexOfSexo)
          val _tmpSueldo: Double
          _tmpSueldo = _stmt.getDouble(_columnIndexOfSueldo)
          val _tmpHijos: Int
          _tmpHijos = _stmt.getLong(_columnIndexOfHijos).toInt()
          val _tmpFoto: String
          _tmpFoto = _stmt.getText(_columnIndexOfFoto)
          val _tmpVersion: Int
          _tmpVersion = _stmt.getLong(_columnIndexOfVersion).toInt()
          _item =
              Docente(_tmpCodigo,_tmpNombres,_tmpApellidos,_tmpSexo,_tmpSueldo,_tmpHijos,_tmpFoto,_tmpVersion)
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
