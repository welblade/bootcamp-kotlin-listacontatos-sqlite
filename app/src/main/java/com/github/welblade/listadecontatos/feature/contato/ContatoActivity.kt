package com.github.welblade.listadecontatos.feature.contato

import android.os.Bundle
import android.view.View
import com.github.welblade.listadecontatos.R
import com.github.welblade.listadecontatos.bases.BaseActivity
import com.github.welblade.listadecontatos.databinding.ActivityContatoBinding
import com.github.welblade.listadecontatos.feature.listacontatos.model.ContatosVO
import com.github.welblade.listadecontatos.singleton.ContatoSingleton

class ContatoActivity : BaseActivity() {
    private lateinit var activityContato : ActivityContatoBinding
    private var index: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        activityContato = ActivityContatoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contato)
        setupToolBar(activityContato.toolBar, "Contato",true)
        setupContato()
        activityContato.btnSalvarConato.setOnClickListener { onClickSalvarContato() }
    }

    private fun setupContato(){
        index = intent.getIntExtra("index",-1)
        if (index == -1){
            activityContato.btnExcluirContato.visibility = View.GONE
            return
        }
        activityContato.etNome.setText(ContatoSingleton.lista[index].nome)
        activityContato.etTelefone.setText(ContatoSingleton.lista[index].telefone)
    }

    private fun onClickSalvarContato(){
        val nome = activityContato.etNome.text.toString()
        val telefone = activityContato.etTelefone.text.toString()
        val contato = ContatosVO(
            0,
            nome,
            telefone
        )
        if(index == -1) {
            ContatoSingleton.lista.add(contato)
        }else{
            ContatoSingleton.lista[index] = contato
        }
        finish()
    }

    fun onClickExcluirContato(view: View) {
        if(index > -1){
            ContatoSingleton.lista.removeAt(index)
            finish()
        }
    }
}
