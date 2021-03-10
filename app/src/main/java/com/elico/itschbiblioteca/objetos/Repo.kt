package com.elico.itschbiblioteca.objetos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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


    fun getUserData (CONDITION:String,CAMPO:String):LiveData<MutableList<MensajesObj>>{
        val mutableData = MutableLiveData<MutableList<MensajesObj>>()
        FirebaseFirestore.getInstance().collection("solicitudes").whereEqualTo("${CAMPO}","${CONDITION}").get().addOnSuccessListener { result ->
            var listData = mutableListOf<MensajesObj>()
            for(document in result){
                listData.add(MensajesObj(document.getString("titulo").toString(),
                        document.getString("autor").toString(),
                        document.getString("editorial").toString(),
                        document.getString("comentario").toString(),
                        document.getString("estado").toString(),
                        document.getString("fecha").toString(),
                        document.getString("respuesta").toString(),
                        document.getString("id").toString()))
            }
            mutableData.value = listData
        }
        return mutableData
    }
}