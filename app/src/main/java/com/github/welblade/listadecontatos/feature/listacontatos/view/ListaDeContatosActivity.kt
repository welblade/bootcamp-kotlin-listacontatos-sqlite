package com.github.welblade.listadecontatos.feature.listacontatos.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.welblade.listadecontatos.bases.BaseActivity
import com.github.welblade.listadecontatos.databinding.ActivityMainBinding
import com.github.welblade.listadecontatos.feature.contato.ContatoActivity
import com.github.welblade.listadecontatos.feature.listacontatos.adapter.ContatoAdapter
import com.github.welblade.listadecontatos.feature.listacontatos.repository.ListaDeContatosRepositoryImpl
import com.github.welblade.listadecontatos.feature.listacontatos.viewmodel.ListaDeContatosViewModel
import com.github.welblade.listadecontatos.helper.DbHelper


class ListaDeContatosActivity : BaseActivity() {
    private lateinit var activityMain: ActivityMainBinding
    private var adapter:ContatoAdapter? = null
    private var contatosViewModel: ListaDeContatosViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMain.root)
        setupToolBar(activityMain.toolBar, "Lista de contatos",false)
        setupListView()
        setupOnClicks()
        setupViewModel()
    }

    private fun setupViewModel(){
        contatosViewModel = ListaDeContatosViewModel(
            contatosRepository = ListaDeContatosRepositoryImpl(
                DbHelper(this)
            )
        )
    }

    private fun setupOnClicks(){
        activityMain.fab.setOnClickListener { onClickAdd() }
        activityMain.ivBuscar.setOnClickListener { onClickBuscar() }
    }

    private fun setupListView(){
        activityMain.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        onClickBuscar()
    }

    private fun onClickAdd(){
        val intent = Intent(this,ContatoActivity::class.java)
        startActivity(intent)
    }

    private fun onClickItemRecyclerView(index: Int){
        val intent = Intent(this,ContatoActivity::class.java)
        intent.putExtra("index", index)
        startActivity(intent)
    }

    private fun onClickBuscar(){
        val busca = activityMain.etBuscar.text.toString()
        contatosViewModel?.getContactList(busca,
            onSuccess = { list ->
                runOnUiThread {
                    adapter = ContatoAdapter(this, list) { onClickItemRecyclerView(it) }
                    activityMain.recyclerView.adapter = adapter
                    activityMain.progress.visibility = View.GONE
                    Toast.makeText(this, "Buscando por $busca", Toast.LENGTH_SHORT).show()
                }
            },
            onFail = {
                runOnUiThread {
                    AlertDialog.Builder(this)
                        .setTitle("Atenção")
                        .setMessage("Não foi possível recuperar sua solicitação.")
                        .setPositiveButton("Ok") { alert, _ ->
                            alert.dismiss()
                        }.show()
                }
            }
        )
    }
}