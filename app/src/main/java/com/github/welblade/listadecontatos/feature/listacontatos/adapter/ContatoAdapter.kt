package com.github.welblade.listadecontatos.feature.listacontatos.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.welblade.listadecontatos.R
import com.github.welblade.listadecontatos.databinding.ItemContatoBinding
import com.github.welblade.listadecontatos.feature.listacontatos.model.ContatosVO

class ContatoAdapter(
    private val context: Context,
    private val lista: List<ContatosVO>,
    private val onClick: ((Int) -> Unit)
) : RecyclerView.Adapter<ContatoViewHolder>() {
    private lateinit var itemContato : ItemContatoBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContatoViewHolder {
        //val view = LayoutInflater.from(context).inflate(R.layout.item_contato,parent,false)
        itemContato = ItemContatoBinding.inflate(LayoutInflater.from(context), parent, false)
        return ContatoViewHolder(itemContato.root)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ContatoViewHolder, position: Int) {
        val contato = lista[position]
        with(holder.itemView){
            itemContato.tvNome.text = contato.nome
            itemContato.tvTelefone.text = contato.telefone
            itemContato.llItem.setOnClickListener { onClick(contato.id) }
        }
    }

}

class ContatoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)