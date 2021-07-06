package com.github.welblade.listadecontatos.helper

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.github.welblade.listadecontatos.feature.listacontatos.model.ContatosVO

class DbHelper(
    context: Context
) : SQLiteOpenHelper(context, NOME_BANCO, null, VERSAO_ATUAL) {
    companion object {
        private val NOME_BANCO = "contato.db"
        private val VERSAO_ATUAL = 1
    }
    val TABLE_NAME = "contato"
    val COLUMNS_ID = "id"
    val COLUMNS_NOME = "nome"
    val COLUMNS_TELEFONE= "telefone"
    val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    val CREATE_TABLE = """
        CREATE TABLE $TABLE_NAME (
            $COLUMNS_ID INTEGER NOT NULL,
            $COLUMNS_NOME TEXT NOT NULL,
            $COLUMNS_TELEFONE TEXT NOT NULL,
            PRIMARY KEY ($COLUMNS_ID AUTOINCREMENT)
    """.trimIndent()
    override fun onCreate(db: SQLiteDatabase?) {
       db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun findContatos(query: String) : List<ContatosVO> {
        val db: SQLiteDatabase = readableDatabase ?: return mutableListOf()
        var lista : MutableList<ContatosVO> = mutableListOf<ContatosVO>()
        val sql = "SELECT * FROM  $TABLE_NAME"
        var cursor : Cursor = db.rawQuery(sql, null) ?: return mutableListOf()
        while(cursor.moveToNext()){
            var contato = ContatosVO(
                cursor.getInt(cursor.getColumnIndex(COLUMNS_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMNS_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMNS_ID))
            )
            lista.add(contato)
        }
        return lista
    }

}