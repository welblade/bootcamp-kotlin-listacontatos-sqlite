package com.github.welblade.listadecontatos.feature.contato

import android.os.Bundle
import android.util.Log
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
        activityContato.progress.visibility = View.VISIBLE
        Thread {

            try {
                var contato = ContatoApplication.instance.helperDb?.findContatoById(idContato)
                idContato = contato?.id ?: -1
                runOnUiThread {
                    activityContato.etNome.setText(contato?.nome)
                    activityContato.etTelefone.setText(contato?.telefone)
                    activityContato.progress.visibility = View.GONE
                }
            } catch (err: Exception) {
                Log.e("DBHELPER", "Consulta: " + err.message.toString())
            }
        }.start()
    }

    private fun onClickSalvarContato(){
        val nome = activityContato.etNome.text.toString()
        val telefone = activityContato.etTelefone.text.toString()
        val contato = ContatosVO(
            idContato,
            nome,
            telefone
        )
        activityContato.progress.visibility = View.VISIBLE
        Thread {
            if (idContato == -1) {
                try {
                    ContatoApplication.instance.helperDb?.saveContato(contato)
                } catch (err: Exception) {
                    Log.e("DBHELPER", "Inserir: " + err.message.toString())
                }
            } else {
                try {
                    ContatoApplication.instance.helperDb?.updateContato(contato)
                } catch (err: Exception) {
                    Log.e("DBHELPER", "Atualizar: " + err.message.toString())
                }
            }
            runOnUiThread {
                activityContato.progress.visibility = View.GONE
                finish()
            }
        }.start()
    }

    fun onClickExcluirContato(view: View) {
        if(idContato > -1){
            activityContato.progress.visibility = View.VISIBLE
            Thread {
                try {
                    ContatoApplication.instance.helperDb?.deleteContato(idContato)
                } catch (err: Exception) {
                    Log.e("DBHELPER", "Deletar: " + err.message.toString())
                }
                runOnUiThread {
                    activityContato.progress.visibility = View.GONE
                    finish()
                }
            }.start()
        }
    }
}