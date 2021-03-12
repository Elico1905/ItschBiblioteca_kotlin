package com.elico.itschbiblioteca.Admin.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elico.itschbiblioteca.Login
import com.elico.itschbiblioteca.R
import kotlinx.android.synthetic.main.fragment_03.view.*

class Fragment03 : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        var view:View = inflater.inflate(R.layout.fragment_03, container, false)
            view.fragment3_user.text = "¡Hola!, ${getNombre()}"


            view.fragment3_salir.setOnClickListener {
                val prefs = activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)?.edit()
                prefs?.clear()
                prefs?.apply()


                val intent:Intent = Intent (activity, Login::class.java)
                startActivity(intent)
                activity?.finish()
            }
        return view
    }



    private fun getNombre(): String {
        val prefs = activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val nombre: String? = prefs?.getString("nombre", "2021ff")
        return "${nombre}"
    }





}