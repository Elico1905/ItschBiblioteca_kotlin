package com.elico.itschbiblioteca

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.core.view.get
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_registro.*
import org.w3c.dom.Text

enum class ProviderType{
   GOOGLE,
    EMAIL
}
class Registro : AppCompatActivity() {
    private val bd = FirebaseFirestore.getInstance()
    private val options = arrayOf("Sistemas","Tic's","Mecatronica","Nanotecnologia","Industrial","Gestion","Biotecnologia")

    private lateinit var fondo_alerta:RelativeLayout
    private lateinit var fondo:RelativeLayout
    private lateinit var animacion: LottieAnimationView
    private lateinit var animacion_alerta: LottieAnimationView
    private lateinit var mensaje:TextView
    private lateinit var boton_alerta:Button
    private lateinit var cadena_mensaje:TextView
    private lateinit var fondo_cancelar:RelativeLayout
    private lateinit var animacion_cancelar:LottieAnimationView
    private lateinit var op_no:Button
    private lateinit var op_si:Button


    private  var NOMBRE:String = ""
    private  var APELLIDOS:String = ""
    private var CARRERA:Int = 0
    private var CORREO:String = ""


    private lateinit var cancelar: Button
    private lateinit var registrar:Button
    private lateinit var correo:EditText
    private lateinit var spinner_id:Spinner
    private lateinit var nombre:EditText
    private lateinit var apellidos:EditText
    private lateinit var control:EditText

    private fun cargar(){
        correo = findViewById(R.id.registro_correo)
        spinner_id = findViewById(R.id.id_spinner)
        cancelar = findViewById(R.id.registro_cancelar)
        registrar = findViewById(R.id.registro_registrar)
        nombre = findViewById(R.id.registro_nombre)
        apellidos = findViewById(R.id.registro_apellidos)
        control = findViewById(R.id.registro_control)

        animacion_alerta = findViewById(R.id.animacion_alerta)
        animacion = findViewById(R.id.animacion_id)
        fondo = findViewById(R.id.fondo)
        fondo_alerta = findViewById(R.id.alerta_mensaje)
        mensaje = findViewById(R.id.animacion_mensaje)
        boton_alerta = findViewById(R.id.boton_alerta)
        cadena_mensaje =findViewById(R.id.cadena_mensaje)
        animacion_cancelar = findViewById(R.id.animacion_cancelar)
        fondo_cancelar = findViewById(R.id.fondo_cancelar)
        op_no = findViewById(R.id.op_no)
        op_si = findViewById(R.id.op_si)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val Bundle = intent.extras

        cargar()
        cargarAnimacion()
        cargarAlerta()

        CORREO = Bundle?.getString("email").toString()
        NOMBRE = Bundle?.getString("name").toString()
        APELLIDOS = Bundle?.getString("apellidos").toString()
        val PROVIDER = Bundle?.getString("provider")

        spinner_id.adapter = ArrayAdapter<String>(this,R.layout.item_perzonalizado_spinner,options)
        cargarDatos()

        boton_alerta.setOnClickListener {
            OcultarAlerta()
        }
        spinner_id.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                CARRERA = position
            }
        }
        registrar.setOnClickListener {
            if (nombre.text.isEmpty() || apellidos.text.isEmpty() || control.text.isEmpty()){
                    MostrarAlerta()
            }else{

                RecargarAnimacion()
                bd.collection("users").document(correo.text.toString()).set(
                    hashMapOf("provider" to PROVIDER,
                        "nombre" to nombre.text.toString(),
                        "apellidos" to apellidos.text.toString(),
                        "control" to control.text.toString(),
                        "carrera" to options.get(CARRERA),
                        "user" to "U"))



                val timer = object: CountDownTimer(5000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        ocultarAnimacion()
                        IrAMenu()
                    }
                }
                val prefs =getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
                prefs.putString("email",CORREO)
                prefs.putString("nombre",nombre.text.toString())
                prefs.putString("apellidos",apellidos.text.toString())
                prefs.putString("control",control.text.toString())
                prefs.putString("carrera",options.get(CARRERA))
                prefs.apply()
                timer.start()

            }

        }
        cancelar.setOnClickListener {
            MostrarCancelar()
        }
        op_no.setOnClickListener {
            OcultarCancelar()
        }
        op_si.setOnClickListener {
            RegresarLogin()
        }
    }

    private fun IrAMenu(){
        val intent: Intent = Intent(this,Menu::class.java)
        startActivity(intent)
        finish()
    }

    private fun RegresarLogin(){
        val intent: Intent = Intent(this,Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun cargarDatos(){
        correo.setText(CORREO)
        nombre.setText(NOMBRE)
        apellidos.setText(APELLIDOS)
    }

    private fun cargarAnimacion(){
        animacion.setAnimation(R.raw.load)
        animacion.repeatCount
        animacion.playAnimation()
    }

    private fun ocultarAnimacion(){
        animacion.loop(false)
        //fondo.visibility = View.GONE
        fondo.visibility = View.INVISIBLE
    }

    private fun RecargarAnimacion(){
        fondo.visibility = View.VISIBLE
        mensaje.setText("Guardando datos...")
        animacion.loop(true)
        animacion.repeatCount
        animacion.playAnimation()

    }

    private fun cargarAlerta(){
        animacion_alerta.setAnimation(R.raw.alerta)

    }

    private fun MostrarAlerta(){
        fondo_alerta.visibility = View.VISIBLE
        animacion_alerta.loop(true)
        animacion_alerta.repeatCount
        animacion_alerta.playAnimation()

    }

    private fun OcultarAlerta(){
        animacion_alerta.loop(false)
        fondo_alerta.visibility = View.INVISIBLE
    }

    private fun MostrarCancelar(){
        fondo_cancelar.visibility = View.VISIBLE
        animacion_cancelar.setAnimation(R.raw.alerta_naranja)
        animacion_cancelar.loop(true)
        animacion_cancelar.repeatCount
        animacion_cancelar.playAnimation()
    }

    private fun OcultarCancelar(){
        animacion_alerta.loop(false)
        fondo_cancelar.visibility = View.INVISIBLE
    }
    override fun onBackPressed() {
        //super.onBackPressed()
        if(fondo_alerta.visibility == View.VISIBLE){
            OcultarAlerta()
        }else{
            if(fondo_cancelar.visibility == View.VISIBLE){
                OcultarCancelar()
            }else{
                MostrarCancelar()
            }
        }



    }
}

