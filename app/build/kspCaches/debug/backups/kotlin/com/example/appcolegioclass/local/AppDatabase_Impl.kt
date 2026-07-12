package com.example.appcolegioclass.local

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.example.appcolegioclass.local.dao.CursoDao
import com.example.appcolegioclass.local.dao.CursoDao_Impl
import com.example.appcolegioclass.local.dao.DocenteDao
import com.example.appcolegioclass.local.dao.DocenteDao_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _docenteDao: Lazy<DocenteDao> = lazy {
    DocenteDao_Impl(this)
  }

  private val _cursoDao: Lazy<CursoDao> = lazy {
    CursoDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(5,
        "55db705227e30b9dd9e74cae51fac291", "08421100c03e86c9387e8f68100086d8") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `docentes` (`codigo` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nombres` TEXT NOT NULL, `apellidos` TEXT NOT NULL, `sexo` TEXT NOT NULL, `sueldo` REAL NOT NULL, `hijos` INTEGER NOT NULL, `foto` TEXT NOT NULL, `version` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `cursos` (`codigo` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nombre` TEXT NOT NULL, `ciclo` TEXT NOT NULL, `creditos` REAL NOT NULL, `carrera` TEXT NOT NULL, `version` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '55db705227e30b9dd9e74cae51fac291')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `docentes`")
        connection.execSQL("DROP TABLE IF EXISTS `cursos`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsDocentes: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsDocentes.put("codigo", TableInfo.Column("codigo", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDocentes.put("nombres", TableInfo.Column("nombres", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDocentes.put("apellidos", TableInfo.Column("apellidos", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDocentes.put("sexo", TableInfo.Column("sexo", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDocentes.put("sueldo", TableInfo.Column("sueldo", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDocentes.put("hijos", TableInfo.Column("hijos", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDocentes.put("foto", TableInfo.Column("foto", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDocentes.put("version", TableInfo.Column("version", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysDocentes: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesDocentes: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoDocentes: TableInfo = TableInfo("docentes", _columnsDocentes, _foreignKeysDocentes,
            _indicesDocentes)
        val _existingDocentes: TableInfo = read(connection, "docentes")
        if (!_infoDocentes.equals(_existingDocentes)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |docentes(com.example.appcolegioclass.local.entidades.Docente).
              | Expected:
              |""".trimMargin() + _infoDocentes + """
              |
              | Found:
              |""".trimMargin() + _existingDocentes)
        }
        val _columnsCursos: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsCursos.put("codigo", TableInfo.Column("codigo", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCursos.put("nombre", TableInfo.Column("nombre", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCursos.put("ciclo", TableInfo.Column("ciclo", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCursos.put("creditos", TableInfo.Column("creditos", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCursos.put("carrera", TableInfo.Column("carrera", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCursos.put("version", TableInfo.Column("version", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysCursos: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesCursos: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoCursos: TableInfo = TableInfo("cursos", _columnsCursos, _foreignKeysCursos,
            _indicesCursos)
        val _existingCursos: TableInfo = read(connection, "cursos")
        if (!_infoCursos.equals(_existingCursos)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |cursos(com.example.appcolegioclass.local.entidades.Curso).
              | Expected:
              |""".trimMargin() + _infoCursos + """
              |
              | Found:
              |""".trimMargin() + _existingCursos)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "docentes", "cursos")
  }

  public override fun clearAllTables() {
    super.performClear(false, "docentes", "cursos")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(DocenteDao::class, DocenteDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(CursoDao::class, CursoDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun docenteDao(): DocenteDao = _docenteDao.value

  public override fun cursoDao(): CursoDao = _cursoDao.value
}
