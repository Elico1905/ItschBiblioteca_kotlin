package com.elico.itschbiblioteca.RecyclerAdmin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.elico.itschbiblioteca.Login
import com.elico.itschbiblioteca.R
import com.elico.itschbiblioteca.objetos.MainViewModel
import com.elico.itschbiblioteca.objetos.MensajesObj
import kotlinx.android.synthetic.main.activity_admin.*;

class Admin : AppCompatActivity() {

    private lateinit var adapter: MainAdapterAdmin

    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    private var lista = mutableListOf<MensajesObj>();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        admin_head_name.text = "¡Hola!, ${getNombre()}"

        adapter = MainAdapterAdmin(this)
        admin_recycler.layoutManager = LinearLayoutManager(this)
        admin_recycler.adapter = adapter
        observeData()



        admin_btn1.setOnClickListener{
            admin_mensaje.text = "Solicitudes Pendientes: ${lista.size}"
        }
        admin_btn2.setOnClickListener {
            admin_mensaje.text = "Reservados: ${lista.size}"
        }

        admin_salir.setOnClickListener {
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()

            val intent: Intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun showFilter(C:Int){
        if (C >= 0){
            println("<<<<< ${C} >>>>>>")
            if (lista.get(C).titulo.contains("y")){
                showItems(C)
            }
            showFilter((C - 1))
        }
    }

    private fun showItems(C:Int){
        println("Titulo: ${lista.get(C).titulo}")
        println(lista.get(C).autor)
        println(lista.get(C).comentario)
        println(lista.get(C).editoarial)
        println(lista.get(C).estatus)
        println(lista.get(C).fecha)
        println(lista.get(C).id)
        println(lista.get(C).respuesta)
        println("------------")
    }

    private fun imprimirall(C:Int){
        if (C >= 0){
            showItems(C)
            imprimirall((C - 1))
        }
    }

    fun observeData(){
        viewModel.fetchUserData("0","estado").observe(this, Observer {
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
            if (adapter.itemCount > 0) {
                admin_recycler.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, "no hay datos", Toast.LENGTH_SHORT).show()
            }
            lista = adapter.returnListData()
            admin_mensaje.text = "Solicitudes Pendientes: ${adapter.itemCount}"
        })

    }



    private fun getNombre(): String {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val nombre: String? = prefs.getString("nombre", "2021ff")
        return "${nombre}"
    }




}