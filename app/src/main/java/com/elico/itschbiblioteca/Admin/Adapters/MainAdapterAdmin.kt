package com.elico.itschbiblioteca.Admin.Adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.elico.itschbiblioteca.R
import com.elico.itschbiblioteca.objetos.MensajesObj
import kotlinx.android.synthetic.main.item_recycler_view_admin.view.*

class MainAdapterAdmin (private val context: Context): RecyclerView.Adapter<MainAdapterAdmin.MainViewHolderAdmin>() {

    private var datalist = mutableListOf<MensajesObj>();

    fun setListData(data:MutableList<MensajesObj>){datalist = data}

    fun returnListData():MutableList<MensajesObj>{ return datalist;}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolderAdmin {
        val view:View = LayoutInflater.from(context).inflate(R.layout.item_recycler_view_admin,parent,false)
        return MainViewHolderAdmin(view)
    }
    override fun getItemCount(): Int { return if(datalist.size>0){ datalist.size}else{ 0 } }


    override fun onBindViewHolder(holder: MainViewHolderAdmin, position: Int) {
        var mensajesObj = datalist[position]
        mensajesObj.autor = if(mensajesObj.autor == ""){"No se agrego autor"}else{mensajesObj.autor}
        mensajesObj.editoarial = if (mensajesObj.editoarial == ""){"No se agrego editorial"}else{mensajesObj.editoarial}
        mensajesObj.comentario = if(mensajesObj.comentario == ""){"No se agrego comentario"}else{mensajesObj.comentario}
        mensajesObj.fecha = mensajesObj.fecha.replace(":","/")
        holder.bindView(mensajesObj)
        holder.itemView.admin_recycler_carta.setOnClickListener{
            Toast.makeText(holder.itemView.context, "${position + 1}", Toast.LENGTH_SHORT).show()
        }
    }

    inner class MainViewHolderAdmin (itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindView(mensajes: MensajesObj){
            itemView.admin_recycler_titulo.text = "${mensajes.titulo}"
            itemView.admin_recycler_autor.text = "Autor: ${mensajes.autor}"
            itemView.admin_recycler_editorial.text = "Editorial: ${mensajes.editoarial}"
            itemView.admin_recycler_comentario.text = "Comentario: ${mensajes.comentario}"
            itemView.admin_recycler_fecha.text = "Fecha de solicitud: ${mensajes.fecha}"


        }
    }
}
