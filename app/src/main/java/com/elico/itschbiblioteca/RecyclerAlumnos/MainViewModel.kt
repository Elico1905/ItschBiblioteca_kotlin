package com.elico.itschbilioteca.RecyclerAlumnos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elico.itschbiblioteca.RecyclerAlumnos.Repo


class MainViewModel: ViewModel() {

    private val repo = Repo()

    fun fetchUserData(CORREO:String):LiveData<MutableList<MensajesObj>>{
        val mutableData = MutableLiveData<MutableList<MensajesObj>>()
        repo.getUserData(CORREO).observeForever{ userList ->
            mutableData.value = userList
        }

        return mutableData
    }
}