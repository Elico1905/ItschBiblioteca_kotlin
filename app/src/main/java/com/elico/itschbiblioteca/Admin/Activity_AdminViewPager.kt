package com.elico.itschbiblioteca.Admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elico.itschbiblioteca.Admin.Adapters.ViewPagerAdapter
import com.elico.itschbiblioteca.Admin.fragments.Fragment01
import com.elico.itschbiblioteca.Admin.fragments.Fragment02
import com.elico.itschbiblioteca.Admin.fragments.Fragment03
import com.elico.itschbiblioteca.R
import kotlinx.android.synthetic.main.activity_admin_view_pager.*

open class Activity_AdminViewPager : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_view_pager)

        setUpTabs()
    }

    private fun setUpTabs(){
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(Fragment01(),"Solicitudes")
        adapter.addFragment(Fragment02(),"Reservados")
        adapter.addFragment(Fragment03(),"Configracion")

        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_email)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_star)
        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_settings)

    }



}