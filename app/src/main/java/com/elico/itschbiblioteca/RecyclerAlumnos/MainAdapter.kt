package com.elico.itschbilioteca.RecyclerAlumnos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.elico.itschbiblioteca.R
import com.elico.itschbiblioteca.objetos.MensajesObj
import kotlinx.android.synthetic.main.items_recycler_view.view.*


class MainAdapter(private val context: Context): RecyclerView.Adapter<MainAdapter.MainViewHolder>(){

    private var datalist = mutableListOf<MensajesObj>();

    fun setListData(data:MutableList<MensajesObj>){datalist = data}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view:View = LayoutInflater.from(context).inflate(R.layout.items_recycler_view,parent,false)
        return MainViewHolder(view)
    }

    override fun getItemCount(): Int { return if(datalist.size>0){ datalist.size}else{ 0 } }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        var mensajesObj = datalist[position]
        mensajesObj.estatus = if (mensajesObj.estatus == "0") {"En proceso"}else{"Terminado"}
        mensajesObj.respuesta = if (mensajesObj.respuesta == "" ){"Aun no hay una respuesta"}else{mensajesObj.respuesta}
        mensajesObj.autor = if(mensajesObj.autor == ""){"No agregaste un autor"}else{mensajesObj.autor}
        mensajesObj.editoarial = if (mensajesObj.editoarial == ""){"No agregasre editorial"}else{mensajesObj.editoarial}
        mensajesObj.comentario = if(mensajesObj.comentario == ""){"No agregaste un comentario"}else{mensajesObj.comentario}
        mensajesObj.fecha = mensajesObj.fecha.replace(":","/")

        holder.bindView(mensajesObj)

    }

    inner class MainViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindView(mensajes: MensajesObj){
            itemView.recycler_titulo.text = "${mensajes.titulo}"
            itemView.recycler_autor.text = "Autor: ${mensajes.autor}"
            itemView.recycler_editorial.text = "Editorial: ${mensajes.editoarial}"
            itemView.recycler_comentario.text = "Comentario: ${mensajes.comentario}"
            itemView.recycler_estatus.text = "Estatus: ${mensajes.estatus}"
            itemView.recycler_fecha.text = "Fecha de solicitud: ${mensajes.fecha}"
            itemView.recycler_respuesta.text = "Respuesta: ${mensajes.respuesta}"
        }
    }
}