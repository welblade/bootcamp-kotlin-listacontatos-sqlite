package com.github.welblade.listadecontatos.feature.contato

import android.os.Bundle
import android.util.Log.*
import android.view.View
import com.github.welblade.listadecontatos.application.ContatoApplication
import com.github.welblade.listadecontatos.bases.BaseActivity
import com.github.welblade.listadecontatos.databinding.ActivityContatoBinding
import com.github.welblade.listadecontatos.feature.listacontatos.model.ContatosVO

class ContatoActivity : BaseActivity() {
    private lateinit var activityContato : ActivityContatoBinding
    private var idContato: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        activityContato = ActivityContatoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(activityContato.root)
        setupToolBar(activityContato.toolBar, "Contato",true)
        setupContato()
        activityContato.btnSalvarConato.setOnClickListener { onClickSalvarContato() }
    }

    private fun setupContato(){
        idContato = intent.getIntExtra("index",-1)
        if (idContato == -1){
            activityContato.btnExcluirContato.visibility = View.GONE
            return
        }
        val contato = ContatoApplication
            .instance.helperDb?.findContatoById(idContato)
        idContato = contato?.id ?: -1
        activityContato.etNome.setText(contato?.nome)
        activityContato.etTelefone.setText(contato?.telefone)
    }

    private fun onClickSalvarContato(){
        val nome = activityContato.etNome.text.toString()
        val telefone = activityContato.etTelefone.text.toString()
        val contato = ContatosVO(
            idContato,
            nome,
            telefone
        )
        if (idContato == -1)
            ContatoApplication.instance.helperDb?.saveContato(contato)
        else
            ContatoApplication.instance.helperDb?.updateContato(contato)
        finish()
    }

    fun onClickExcluirContato(view: View) {
        if(idContato > -1){
            ContatoApplication.instance.helperDb?.deleteContato(idContato)
            finish()
        }
    }
}
