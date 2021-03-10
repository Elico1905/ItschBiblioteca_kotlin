package com.elico.itschbiblioteca

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.widget.addTextChangedListener
import com.elico.itschbiblioteca.RecyclerAlumnos.Mensajes
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_menu.*
import java.text.SimpleDateFormat
import java.util.*

open class Menu : AppCompatActivity() {

    private val bd = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        setContentView(R.layout.activity_menu)

        head_name.text = "¡Hola!, ${getNombre()}"
        cargarRutasAnimaciones()
        StartAnimaciones()

        op_no_consultar.setOnClickListener {
            ocultarConsultar()
        }
        op_si_consultar.setOnClickListener {
            ValidarCampos()
        }

        op_confirmar_si.setOnClickListener {
            registrarSolicitud()
            borrarAlerta()
            DesactivarConfirmar()
            fondo_consultar.visibility = View.INVISIBLE

        }
        op_confirmar_no.setOnClickListener {
            DesactivarConfirmar()
        }
        comentario.addTextChangedListener {
            comentario_conteo.setText(comentario.length().toString() + "/100")
        }
        op_salir.setOnClickListener {
            EliminarSharedPreferences()
            val intent: Intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
        op_cancelar.setOnClickListener {
            CancelarSalir()
        }

        menu_salir.setOnClickListener {
            Salir()
        }
        menu_mis_libros.setOnClickListener {
            //val intent: Intent = Intent(this, Mensajes::class.java)
            val intent: Intent = Intent(this, Mensajes::class.java)
            startActivity(intent)

        }
        menu_add_libro.setOnClickListener {
            protector()
            mostrarConsultar()
        }
    }

    private fun borrarAlerta() {
        consulta_titulo.setText("")
        consulta_editorial.setText("")
        consulta_autor.setText("")
        comentario.setText("")
    }


    private fun cargarRutasAnimaciones() {
        animacion_mensaje.setAnimation(R.raw.mensajes)
        animacion_salir.setAnimation(R.raw.salir)
        animacion_libro.setAnimation(R.raw.libro)
        animacion_confirmar.setAnimation(R.raw.alerta_naranja)
    }

    private fun StartAnimaciones() {

        animacion_mensaje.loop(true)
        animacion_mensaje.repeatCount
        animacion_mensaje.playAnimation()

        animacion_libro.loop(true)
        animacion_libro.repeatCount
        animacion_libro.playAnimation()
    }

    private fun EliminarSharedPreferences() {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.clear()
        prefs.apply()
    }

    private fun message(cadena: String) {
        Toast.makeText(this, cadena, Toast.LENGTH_LONG).show()
    }


    private fun ActivarConfirmar() {
        fondo_confirmar.visibility = View.VISIBLE
        animacion_confirmar.loop(true)
        animacion_confirmar.repeatCount
        animacion_confirmar.playAnimation()
    }

    private fun DesactivarConfirmar() {
        animacion_confirmar.loop(false)
        fondo_confirmar.visibility = View.INVISIBLE
    }

    private var estado: Boolean = false

    private fun ValidarCampos() {
        if (consulta_titulo.text.isEmpty()) {
            if (estado == false) {
                estado = true
                val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
                TOAST_menu.startAnimation(animation)
                TOAST_menu.visibility = View.VISIBLE
                TOAST_mensaje.text = "¡Porfavor escribe el titulo!"

                val timer = object : CountDownTimer(2000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_out)
                        TOAST_menu.startAnimation(animation)
                        TOAST_menu.visibility = View.INVISIBLE
                        estado = false
                    }
                }
                timer.start()
            }

        } else {
            ActivarConfirmar()
            mensaje_confirmar.text = "¿Seguro que deseas solicitar el libro?"
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }

    private fun mostrarConsultar() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        fondo_consultar.startAnimation(animation)
        fondo_consultar.visibility = View.VISIBLE

        val animation2 = AnimationUtils.loadAnimation(this, R.anim.slide_down)
        cardView_consultar.startAnimation(animation2)
        cardView_consultar.visibility = View.VISIBLE

    }

    private fun ocultarConsultar() {
        protector()
        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        fondo_consultar.startAnimation(animation)
        fondo_consultar.visibility = View.INVISIBLE


        var animation2 = AnimationUtils.loadAnimation(this, R.anim.slide_up)

        cardView_consultar.startAnimation(animation2)
        cardView_consultar.visibility = View.INVISIBLE
        borrarAlerta()
    }

    private fun Salir() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in_2)
        fondo_salir.startAnimation(animation)
        fondo_salir.visibility = View.VISIBLE

        animacion_salir.loop(true)
        animacion_salir.repeatCount
        animacion_salir.playAnimation()
    }

    private fun CancelarSalir() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_out_2)
        fondo_salir.startAnimation(animation)
        animacion_salir.loop(false)
        fondo_salir.visibility = View.INVISIBLE
    }

    private fun protector() {
        val view = this.currentFocus
        if (view != null) {
            val hideMe = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hideMe.hideSoftInputFromWindow(view.windowToken, 0)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        menu_protector.visibility = View.VISIBLE
        val timer = object : CountDownTimer(800, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                menu_protector.visibility = View.INVISIBLE
            }
        }
        timer.start()
    }

    private fun registrarSolicitud() {
        var crearID:String = "${LeerControl()}_${getSystemData()}"
        bd.collection("solicitudes").document("${crearID}").set(
                hashMapOf("titulo" to consulta_titulo.text.toString(),
                        "editorial" to consulta_editorial.text.toString(),
                        "autor" to consulta_autor.text.toString(),
                        "fecha" to getFecha(),
                        "hora" to getHora(),
                        "control" to LeerControl(),
                        "correo" to getCorreo(),
                        "estado" to "0",
                        "comentario" to comentario.text.toString(),
                        "respuesta" to "",
                        "nombre_alumno" to getNombre(),
                        "apellidos_alumno" to getApellidos(),
                        "id" to "${crearID}"))


        if (estado2 == false) {
            estado2 = true
            val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            TOAST_menu_2.startAnimation(animation)
            TOAST_menu_2.visibility = View.VISIBLE
            TOAST_mensaje_2.text = "¡Tu solicitud se envio!"

            val timer = object : CountDownTimer(2000, 1000) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_out)
                    TOAST_menu_2.startAnimation(animation)
                    TOAST_menu_2.visibility = View.INVISIBLE
                    estado2 = false
                }
            }
            timer.start()
        }
    }

    private var estado2: Boolean = false

    private fun getNombre(): String {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val nombre: String? = prefs.getString("nombre", "2021ff")
        return "${nombre}"
    }

    private fun getApellidos(): String {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val apellidos: String? = prefs.getString("apellidos", "2021ff")
        return "${apellidos}"
    }

    private fun getCorreo(): String {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val correo: String? = prefs.getString("email", "2021ff")
        return "${correo}"
    }

    private fun getHora(): String {
        val sdf = SimpleDateFormat("HH:mm:ss")
        val currentDate = sdf.format(Date())
        return "${currentDate}"
    }

    private fun getFecha(): String {
        val sdf = SimpleDateFormat("dd:M:yyyy")
        val currentDate = sdf.format(Date())
        return "${currentDate}"
    }

    private fun getSystemData(): String {
        return "${getFecha()}_${getHora()}"
    }

    private fun LeerControl(): String {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val Control: String? = prefs.getString("control", "2021ff")
        return "${Control}"
    }


}
