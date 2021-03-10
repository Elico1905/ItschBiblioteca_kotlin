package com.elico.itschbiblioteca.objetos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elico.itschbiblioteca.objetos.MensajesObj
import com.elico.itschbiblioteca.objetos.Repo


class MainViewModel: ViewModel() {

    private val repo = Repo()

    fun fetchUserData(DATO:String,CAMPO:String):LiveData<MutableList<MensajesObj>>{
        val mutableData = MutableLiveData<MutableList<MensajesObj>>()
        repo.getUserData(DATO,"${CAMPO}").observeForever{ userList ->
            mutableData.value = userList
        }

        return mutableData
    }
}