package com.github.welblade.listadecontatos.feature.listacontatos

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.welblade.listadecontatos.R
import com.github.welblade.listadecontatos.application.ContatoApplication
import com.github.welblade.listadecontatos.bases.BaseActivity
import com.github.welblade.listadecontatos.databinding.ActivityMainBinding
import com.github.welblade.listadecontatos.feature.contato.ContatoActivity
import com.github.welblade.listadecontatos.feature.listacontatos.adapter.ContatoAdapter
import com.github.welblade.listadecontatos.feature.listacontatos.model.ContatosVO
import com.github.welblade.listadecontatos.singleton.ContatoSingleton
import java.lang.Exception


class MainActivity : BaseActivity() {
    private lateinit var activityMain: ActivityMainBinding
    private var adapter:ContatoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        geraListaDeContatos()
        setupToolBar(activityMain.toolBar, "Lista de contatos",false)
        setupListView()
        setupOnClicks()
    }

    private fun setupOnClicks(){
        activityMain.fab.setOnClickListener { onClickAdd() }
        activityMain.ivBuscar.setOnClickListener { onClickBuscar() }
    }

    private fun setupListView(){
        activityMain.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ContatoAdapter(this,ContatoSingleton.lista) {onClickItemRecyclerView(it)}
        activityMain.recyclerView.adapter = adapter
    }

    private fun geraListaDeContatos(){
        ContatoSingleton.lista.add(ContatosVO(1,"Fulano", "(00) 9900-0001"))
        ContatoSingleton.lista.add(ContatosVO(2,"Ciclano", "(00) 9900-0002"))
        ContatoSingleton.lista.add(ContatosVO(3,"Vinicius", "(00) 9900-0001"))
    }

    override fun onResume() {
        super.onResume()
        adapter?.notifyDataSetChanged()
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
        var listaFiltrada: List<ContatosVO> = mutableListOf()
        try {
            listaFiltrada = ContatoApplication
                .instance.helperDb?.findContatos(busca) ?: mutableListOf()
        } catch (err : Exception){
            err.printStackTrace()
        }
        adapter = ContatoAdapter(this,listaFiltrada) {onClickItemRecyclerView(it)}
        activityMain.recyclerView.adapter = adapter
        Toast.makeText(this,"Buscando por $busca",Toast.LENGTH_SHORT).show()
    }

}
