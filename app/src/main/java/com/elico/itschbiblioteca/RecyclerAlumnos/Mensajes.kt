package com.elico.itschbiblioteca.RecyclerAlumnos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.elico.itschbiblioteca.R
import com.elico.itschbilioteca.RecyclerAlumnos.MainAdapter
import com.elico.itschbiblioteca.objetos.MainViewModel
import kotlinx.android.synthetic.main.activity_mensajes.*

class Mensajes : AppCompatActivity() {

    private lateinit var adapter:MainAdapter

    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mensajes)
        MostrarCargar()

        adapter = MainAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        observeData()
    }


    fun observeData(){
        viewModel.fetchUserData(getCorreo(),"correo").observe(this, Observer {
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
            if(adapter.itemCount <= 0){
                msm_recycler.visibility = View.VISIBLE
            }
            OcultarCargar()
        })


    }
    private fun getCorreo(): String {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE)
        val correo: String? = prefs.getString("email", "2021ff")
        return "${correo}"
    }

    private fun MostrarCargar(){
        recycler_animacion.setAnimation(R.raw.load)
        recycler_fondo.visibility = View.VISIBLE
        recycler_animacion.loop(true)
        recycler_animacion.repeatCount
        recycler_animacion.playAnimation()
    }

    private fun OcultarCargar(){
        recycler_fondo.visibility = View.GONE
        recycler_animacion.pauseAnimation()
    }
}

