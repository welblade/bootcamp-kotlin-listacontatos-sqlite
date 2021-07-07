package com.github.welblade.listadecontatos.feature.contato

import android.os.Bundle
import android.util.Log
import android.util.Log.*
import android.view.View
import com.github.welblade.listadecontatos.R
import com.github.welblade.listadecontatos.application.ContatoApplication
import com.github.welblade.listadecontatos.bases.BaseActivity
import com.github.welblade.listadecontatos.databinding.ActivityContatoBinding
import com.github.welblade.listadecontatos.feature.listacontatos.model.ContatosVO
import com.github.welblade.listadecontatos.singleton.ContatoSingleton

class ContatoActivity : BaseActivity() {
    private lateinit var activityContato : ActivityContatoBinding
    private var id : Int = 0
    private var index: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        activityContato = ActivityContatoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(activityContato.root)
        setupToolBar(activityContato.toolBar, "Contato",true)
        setupContato()
        activityContato.btnSalvarConato.setOnClickListener { onClickSalvarContato() }
    }

    private fun setupContato(){
        index = intent.getIntExtra("index",-1)
        Log.e("INDEX", "valor de index $index")
        if (index == -1){
            activityContato.btnExcluirContato.visibility = View.GONE
            return
        }else {
            val contato = ContatoApplication
                .instance.helperDb?.findContatoById(index)
            id = contato?.id ?: 0
            activityContato.etNome.setText(contato?.nome)
            activityContato.etTelefone.setText(contato?.telefone)
        }
    }

    private fun onClickSalvarContato(){
        val nome = activityContato.etNome.text.toString()
        val telefone = activityContato.etTelefone.text.toString()
        val contato = ContatosVO(
            id,
            nome,
            telefone
        )
        if (contato.id == 0)
            ContatoApplication.instance.helperDb?.saveContato(contato)
        else
            ContatoApplication.instance.helperDb?.updateContato(contato)
        finish()
    }

    fun onClickExcluirContato(view: View) {
        if(index > -1){
            ContatoSingleton.lista.removeAt(index)
            finish()
        }
    }
}
