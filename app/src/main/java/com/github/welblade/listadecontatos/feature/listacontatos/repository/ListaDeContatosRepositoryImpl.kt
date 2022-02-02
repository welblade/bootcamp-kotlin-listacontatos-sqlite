package com.github.welblade.listadecontatos.feature.listacontatos.repository

import android.database.Cursor
import android.database.SQLException
import com.github.welblade.listadecontatos.feature.listacontatos.model.ContatosVO
import com.github.welblade.listadecontatos.helper.*

class ListaDeContatosRepositoryImpl(
    private val helperDb: DbHelper?
) {
    fun requestListaDeContatos(
        search: String,
        onSuccess: ((List<ContatosVO>)->Unit),
        onFail: ((Throwable)->Unit)
    ){
        try {
            val db = helperDb?.readableDatabase
            db?.let {
                val lista : MutableList<ContatosVO> = mutableListOf()

                val where = "$COLUMNS_NOME like ?"
                val values = arrayOf("%$search%")
                val cursor : Cursor = it.query(TABLE_NAME, null,where, values, null, null,  null)
                while(cursor.moveToNext()){
                    val contato = ContatosVO(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMNS_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMNS_NOME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMNS_TELEFONE))
                    )
                    lista.add(contato)
                }
                cursor.close()
                onSuccess(lista)
            } ?: throw SQLException("Não foi possível executar a consulta.")
        }catch (e: Throwable){
            onFail(e)
        }
    }
}