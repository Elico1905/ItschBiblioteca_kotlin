package com.elico.itschbiblioteca.objetos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.elico.itschbiblioteca.Login

class SplashScreen : AppCompatActivity() {

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    private val runnable = Runnable {
        if (!isFinishing){
            startActivity(Intent(applicationContext, Login::class.java))
            //startActivity(Intent(applicationContext, AdminViewPager::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable,100)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }
}
