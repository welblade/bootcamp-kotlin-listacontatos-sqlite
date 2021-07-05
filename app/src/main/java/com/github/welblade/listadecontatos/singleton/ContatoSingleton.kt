package com.github.welblade.listadecontatos.singleton

import com.github.welblade.listadecontatos.feature.listacontatos.model.ContatosVO

object ContatoSingleton {
    var lista: MutableList<ContatosVO> = mutableListOf()
}