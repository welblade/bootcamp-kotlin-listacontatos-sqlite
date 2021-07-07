package com.github.welblade.listadecontatos.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.github.welblade.listadecontatos.feature.listacontatos.model.ContatosVO

class DbHelper(
    context: Context
) : SQLiteOpenHelper(context, NOME_BANCO, null, VERSAO_ATUAL) {
    companion object {
        private const val NOME_BANCO = "contato.db"
        private const val VERSAO_ATUAL = 4
    }
    private val TABLE_NAME = "contato"
    private val COLUMNS_ID = "id"
    private val COLUMNS_NOME = "nome"
    private val COLUMNS_TELEFONE= "telefone"
    private val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    private val CREATE_TABLE = """
        CREATE TABLE $TABLE_NAME (
            $COLUMNS_ID INTEGER NOT NULL,
            $COLUMNS_NOME TEXT NOT NULL,
            $COLUMNS_TELEFONE TEXT NOT NULL,
            PRIMARY KEY ($COLUMNS_ID AUTOINCREMENT)
        )
    """.trimIndent()
    override fun onCreate(db: SQLiteDatabase?) {
       db?.execSQL(CREATE_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if(oldVersion != newVersion){
            db?.execSQL(DROP_TABLE)
        }
        onCreate(db)
    }

    fun findContatos(query: String) : List<ContatosVO> {
        val db: SQLiteDatabase = readableDatabase ?: return mutableListOf()
        val lista : MutableList<ContatosVO> = mutableListOf()

        val where = "$COLUMNS_NOME like ?"
        val values = arrayOf("%$query%")
        val cursor : Cursor = db.query(TABLE_NAME, null,where, values, null, null,  null)
        while(cursor.moveToNext()){
            val contato = ContatosVO(
                cursor.getInt(cursor.getColumnIndex(COLUMNS_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMNS_NOME)),
                cursor.getString(cursor.getColumnIndex(COLUMNS_TELEFONE))
            )
            lista.add(contato)
        }
        cursor.close()
        return lista
    }
    fun findContatoById(id:Int) : ContatosVO?{
        val db: SQLiteDatabase = readableDatabase ?: return null
        val sql = "SELECT * FROM  $TABLE_NAME WHERE $COLUMNS_ID = ?"
        val cursor : Cursor = db.rawQuery(sql, arrayOf("$id")) ?: return null

        return ContatosVO(
            cursor.getInt(cursor.getColumnIndex(COLUMNS_ID)),
            cursor.getString(cursor.getColumnIndex(COLUMNS_NOME)),
            cursor.getString(cursor.getColumnIndex(COLUMNS_TELEFONE))
        )

       // return null
    }
    fun saveContato(contato : ContatosVO){
        val db: SQLiteDatabase = writableDatabase ?: return
        val sql = """
            INSERT INTO $TABLE_NAME ($COLUMNS_NOME, $COLUMNS_TELEFONE)
            VALUES (?, ?)
        """.trimIndent()
        db.execSQL(sql, arrayOf(contato.nome, contato.telefone))
    }
    fun updateContato(contato : ContatosVO){
        val db: SQLiteDatabase = writableDatabase ?: return
        val where = "$COLUMNS_ID == ?"
        val values = ContentValues().apply {
            put(COLUMNS_NOME, contato.nome)
            put(COLUMNS_TELEFONE, contato.telefone)
        }
        db.update(TABLE_NAME, values, where, arrayOf("${contato.id}"))
    }
}