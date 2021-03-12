package com.elico.itschbiblioteca.Admin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.elico.itschbiblioteca.R
import com.elico.itschbiblioteca.Admin.Adapters.MainAdapterAdmin
import com.elico.itschbiblioteca.objetos.MainViewModel
import com.elico.itschbiblioteca.objetos.MensajesObj
import kotlinx.android.synthetic.main.fragment_01.view.*


class Fragment01 : Fragment() {

    private lateinit var adapter: MainAdapterAdmin

    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    private var lista = mutableListOf<MensajesObj>();


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view:View = inflater.inflate(R.layout.fragment_01, container, false)

        adapter = MainAdapterAdmin(view.context)
        view.admin_recycler.layoutManager = LinearLayoutManager(view.context)
        view.admin_recycler.adapter = adapter
        observeData(view)
        return view
    }

    fun observeData(view:View){
        viewModel.fetchUserData("0","estado").observe(viewLifecycleOwner, Observer {
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
            if (adapter.itemCount > 0) {
                view.admin_recycler.visibility = View.VISIBLE
            } else {
                Toast.makeText(context, "no hay datos", Toast.LENGTH_SHORT).show()
            }
            lista = adapter.returnListData()
        })

    }

}