package com.elico.itschbiblioteca.RecyclerAlumnos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elico.itschbilioteca.RecyclerAlumnos.MensajesObj
import com.google.firebase.firestore.FirebaseFirestore

class Repo{
/*
    fun getUserData ():LiveData<MutableList<MensajesObj>>{
        val mutableData = MutableLiveData<MutableList<MensajesObj>>()
        FirebaseFirestore.getInstance().collection("solicitudes").get().addOnSuccessListener { result ->
            var listData = mutableListOf<MensajesObj>()
            for(document in result){
                listData.add(MensajesObj(document.getString("titulo").toString(),
                        document.getString("autor").toString(),
                        document.getString("editorial").toString(),
                        document.getString("comentario").toString(),
                        document.getString("estado").toString(),
                        document.getString("fecha").toString(),
                        document.getString("respuesta").toString()))
            }
            mutableData.value = listData
        }
        return mutableData
    }

 */


    fun getUserData (CORREO:String):LiveData<MutableList<MensajesObj>>{
        val mutableData = MutableLiveData<MutableList<MensajesObj>>()
        FirebaseFirestore.getInstance().collection("solicitudes").whereEqualTo("correo","${CORREO}").get().addOnSuccessListener { result ->
            var listData = mutableListOf<MensajesObj>()
            for(document in result){
                listData.add(MensajesObj(document.getString("titulo").toString(),
                        document.getString("autor").toString(),
                        document.getString("editorial").toString(),
                        document.getString("comentario").toString(),
                        document.getString("estado").toString(),
                        document.getString("fecha").toString(),
                        document.getString("respuesta").toString()))
            }
            mutableData.value = listData
        }
        return mutableData
    }
}