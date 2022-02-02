package com.github.welblade.listadecontatos.feature.listacontatos.viewmodel

import androidx.lifecycle.ViewModel
import com.github.welblade.listadecontatos.feature.listacontatos.model.ContatosVO
import com.github.welblade.listadecontatos.feature.listacontatos.repository.ListaDeContatosRepositoryImpl

class ListaDeContatosViewModel(
    private val contatosRepository: ListaDeContatosRepositoryImpl?
) : ViewModel() {

    fun getContactList(
        search: String,
        onSuccess: ((List<ContatosVO>)->Unit),
        onFail: ((Throwable)->Unit)
    ){
        Thread {
            Runnable {
                contatosRepository?.requestListaDeContatos(
                    search,
                    onSuccess = { onSuccess(it) },
                    onFail = {onFail(it)}
                )
            }
        }.start()
    }
}