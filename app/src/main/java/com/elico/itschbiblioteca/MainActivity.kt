package com.elico.itschbiblioteca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var animacion: LottieAnimationView
    private lateinit var boton:Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //DefaultGetData()


        animacion = findViewById(R.id.animacion)
        boton = findViewById(R.id.boton_animacion)

        boton.setOnClickListener {
            animacion.loop(false)

        }

        animacion.setAnimation(R.raw.alerta_naranja_2)
        animacion.repeatCount
        animacion.playAnimation()

    }

    private val bd = FirebaseFirestore.getInstance()
    private fun DefaultGetData(){
        println("empieza")
        bd.collection("users")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        println("${document.id} => ${document.data}")
                    }
                }
                .addOnFailureListener { exception ->
                    print("Error getting documents:"+exception)
                }
        println("termina")
    }
}