package com.elico.itschbiblioteca

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.airbnb.lottie.LottieAnimationView
import com.elico.itschbiblioteca.RecyclerAdmin.Admin
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore


class Login : AppCompatActivity() {
    private lateinit var fondo_correo: RelativeLayout

    private val GOOGLE_SING_IN = 100
    private val bd = FirebaseFirestore.getInstance()


    private lateinit var fondo_oscuro: RelativeLayout
    private lateinit var TOAST: RelativeLayout
    private lateinit var CORREO: TextView
    private lateinit var PASS: TextView
    private lateinit var fondo_animacion: RelativeLayout
    private lateinit var animacion: LottieAnimationView
    private lateinit var animacion_ms_mal: LottieAnimationView
    private lateinit var equis: ImageView
    private lateinit var menu_salir_error: ImageView
    private var SharedCorreo: String = ""
    private var SharedNombre: String = ""
    private var SharedApellidos: String = ""
    private lateinit var provedor: ProviderType
    private lateinit var fondo_ms_mal: RelativeLayout
    private var estado: Boolean = false
    private lateinit var fondo_ocuro: RelativeLayout
    private lateinit var protector: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        setContentView(R.layout.activity_login)

        fondo_oscuro = findViewById(R.id.fondo_oscuro)
        protector = findViewById(R.id.login_protector)
        TOAST = findViewById(R.id.TOAST)
        menu_salir_error = findViewById(R.id.menu_salir_error)
        fondo_animacion = findViewById(R.id.fondo_animacion)
        animacion = findViewById(R.id.animacion_id_login)
        equis = findViewById(R.id.menu_salir)
        fondo_correo = findViewById(R.id.menu_correo_add)
        animacion_ms_mal = findViewById(R.id.animacion_ms_mal)
        CORREO = findViewById(R.id.menu_correo)
        PASS = findViewById(R.id.menu_pass)
        fondo_ms_mal = findViewById(R.id.fondo_ms_mal)
        fondo_ocuro = findViewById(R.id.fondo_oscuro)

        CargarAnimacion()
        Leerferencias()

        var Button_Google: CardView = findViewById(R.id.cardView_google)
        var Button_Correo: CardView = findViewById(R.id.cardView_correo)
        var Button_entrar: Button = findViewById(R.id.menu_entrar)
        var Button_registrar: Button = findViewById(R.id.menu_regitrar)


        menu_salir_error.setOnClickListener {
            OcultarMensajeError()
        }

        Button_entrar.setOnClickListener {
            if (!CORREO.text.isEmpty() && !PASS.text.isEmpty()) {
                MostrarAnimacion()
                FirebaseAuth.getInstance().signInWithEmailAndPassword(CORREO.text.toString(), PASS.text.toString())
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                SharedCorreo = CORREO.text.toString()
                                SharedNombre = ""
                                SharedApellidos = ""
                                provedor = ProviderType.EMAIL
                                GetDatosFirebase(CORREO.text.toString())
                            } else {
                                OcultarAnimacion()
                                MostrarMensajeError()
                            }
                        }
            } else {
                FaltanDatos()
            }
        }
        Button_registrar.setOnClickListener {
            if (!CORREO.text.isEmpty() && !PASS.text.isEmpty()) {
                MostrarAnimacion()
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(CORREO.text.toString(), PASS.text.toString())
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                SharedCorreo = CORREO.text.toString()
                                SharedNombre = ""
                                SharedApellidos = ""
                                provedor = ProviderType.EMAIL
                                GetDatosFirebase(CORREO.text.toString())
                            } else {
                                OcultarAnimacion()
                                MostrarMensajeError()
                            }
                        }
            } else {
                FaltanDatos()
            }
        }

        equis.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.slide_right)
            val animation_fondo = AnimationUtils.loadAnimation(this, R.anim.fade_out)

            protector()


            fondo_oscuro.startAnimation(animation_fondo)
            fondo_correo.startAnimation(animation)

            fondo_correo.visibility = View.INVISIBLE
            fondo_oscuro.visibility = View.INVISIBLE
            EliminarCamposEmail()
        }

        Button_Correo.setOnClickListener {
            val animation_fondo = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            val animation = AnimationUtils.loadAnimation(this, R.anim.slide_left)

            fondo_ocuro.startAnimation(animation_fondo)
            fondo_correo.startAnimation(animation)

            fondo_correo.visibility = View.VISIBLE
            fondo_oscuro.visibility = View.VISIBLE
        }

        Button_Google.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, GOOGLE_SING_IN)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SING_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    MostrarAnimacion()
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            //showHome(account.email?:"",account.givenName?:"",account.familyName?:"",ProviderType.GOOGLE)
                            //consultamos a firebase que el usuario este registrado
                            SharedCorreo = account.email ?: ""
                            SharedNombre = account.givenName ?: ""
                            SharedApellidos = account.familyName ?: ""
                            provedor = ProviderType.GOOGLE
                            GetDatosFirebase(account.email ?: "")
                        } else {
                            //error de que no hay cuenta seleccionada
                        }
                    }

                }
            } catch (e: ApiException) {
            }

        }
    }


    private fun Leerferencias() {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val localCorreo: String? = prefs.getString("email", "2021ff")
        val localNombre: String? = prefs.getString("nombre", "2021ff")
        val localApellidos: String? = prefs.getString("apellidos", "2021ff")
        val localControl: String? = prefs.getString("control", "2021ff")
        val localCarrera: String? = prefs.getString("carrera", "2021ff")
        val localUser: String? = prefs.getString("user", "2021ff")
        if (!localCorreo.equals("2021ff") || !localNombre.equals("2021ff") || !localApellidos.equals("2021ff")
                || !localControl.equals("2021ff") || !localCarrera.equals("2021ff")) {
            Toast.makeText(this, "${localUser}", Toast.LENGTH_SHORT).show()
            if (localUser.equals("A")) {
                val intent: Intent = Intent(this, Admin::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent: Intent = Intent(this, Menu::class.java)
                startActivity(intent)
                finish()
            }

        }

    }

    private fun GetDatosFirebase(authcorreo: String) {
        bd.collection("users").document(authcorreo).get().addOnSuccessListener {

            val nombre: String = it.get("nombre").toString()
            val apellidos: String = it.get("apellidos").toString()
            val carrera: String = it.get("carrera").toString()
            val user: String = it.get("user").toString()
            val control: String = it.get("control").toString()

            if (!user.equals("null")) {
                if (user == "A") {
                    //pasar a home admin
                    val homeIntent = Intent(this, Admin::class.java)
                    startActivity(homeIntent)
                    finish()

                    SaveSharedpreferes(authcorreo, nombre, apellidos, control, carrera, user)
                    ocultarAnimacion("home")
                } else {
                    //es un user U
                    if (nombre.isNullOrEmpty() || apellidos.isNullOrEmpty() || carrera.isNullOrEmpty() || user.isNullOrEmpty() || control.isNullOrEmpty()) {
                        //faltan datos, mandar a registro
                        ocultarAnimacion("registro")
                    } else {
                        //todo bien mandar menu
                        SaveSharedpreferes(authcorreo, nombre, apellidos, control, carrera, user)
                        ocultarAnimacion("menu")
                    }
                }
            } else {
                //no esta registrado
                ocultarAnimacion("registro")
            }

        }
    }

    private fun message(cadena: String) {
        Toast.makeText(this, cadena, Toast.LENGTH_LONG).show()
    }


    private fun CargarAnimacion() {
        animacion.setAnimation(R.raw.load)
        animacion_ms_mal.setAnimation(R.raw.alerta_naranja_2)
    }

    private fun MostrarAnimacion() {
        fondo_animacion.visibility = View.VISIBLE
        animacion.loop(true)
        animacion.repeatCount
        animacion.playAnimation()
    }

    private fun ocultarAnimacion(cadena: String) {
        val timer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                if (cadena.equals("menu")) {
                    Gomenu()
                }
                if (cadena.equals("registro")) {
                    GoRegistro()
                }
                if (cadena.equals("home")) {
                    message("te lleva a home del admin")
                }
            }
        }
        timer.start()
    }

    private fun GoRegistro() {
        val homeIntent = Intent(this, Registro::class.java).apply {
            putExtra("email", SharedCorreo)
            putExtra("name", SharedNombre)
            putExtra("apellidos", SharedApellidos)
            putExtra("provider", provedor.toString())
        }
        animacion.pauseAnimation()
        startActivity(homeIntent)
        finish()
    }

    private fun Gomenu() {
        animacion.pauseAnimation()
        val intent: Intent = Intent(this, Menu::class.java)
        startActivity(intent)
        finish()
    }

    private fun SaveSharedpreferes(correo: String, nombre: String, apellidos: String, control: String, carrera: String, user: String) {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", correo)
        prefs.putString("nombre", nombre)
        prefs.putString("apellidos", apellidos)
        prefs.putString("control", control)
        prefs.putString("carrera", carrera)
        prefs.putString("user", user)
        prefs.apply()
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        if (fondo_correo.visibility == View.VISIBLE && fondo_ms_mal.visibility == View.INVISIBLE) {
            val animation = AnimationUtils.loadAnimation(this, R.anim.slide_right)
            val animation_fondo = AnimationUtils.loadAnimation(this, R.anim.fade_out)

            protector()

            fondo_oscuro.startAnimation(animation_fondo)
            fondo_correo.startAnimation(animation)

            fondo_correo.visibility = View.INVISIBLE
            fondo_oscuro.visibility = View.INVISIBLE
            EliminarCamposEmail()
        }
    }

    private fun OcultarAnimacion() {
        fondo_animacion.visibility = View.INVISIBLE
        animacion.pauseAnimation()
    }

    private fun MostrarMensajeError() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        fondo_ms_mal.startAnimation(animation)

        fondo_ms_mal.visibility = View.VISIBLE
        animacion_ms_mal.loop(true)

        animacion_ms_mal.repeatCount
        animacion_ms_mal.playAnimation()
    }

    private fun OcultarMensajeError() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        fondo_ms_mal.startAnimation(animation)
        fondo_ms_mal.visibility = View.INVISIBLE
        animacion_ms_mal.pauseAnimation()
    }

    private fun EliminarCamposEmail() {
        CORREO.text = ""
        PASS.text = ""
    }


    private fun FaltanDatos() {
        if (estado == false) {
            estado = true
            val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            TOAST.startAnimation(animation)
            TOAST.visibility = View.VISIBLE
            val timer = object : CountDownTimer(3000, 1000) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_out)
                    TOAST.startAnimation(animation)
                    TOAST.visibility = View.INVISIBLE
                    estado = false
                }
            }
            timer.start()
        }

    }

    private fun protector() {
        val view = this.currentFocus
        if (view != null) {
            val hideMe = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hideMe.hideSoftInputFromWindow(view.windowToken, 0)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        protector.visibility = View.VISIBLE
        //val timer = object : CountDownTimer(1500, 1000) {
        //override fun onTick(millisUntilFinished: Long) {}
        //override fun onFinish() {
        protector.visibility = View.INVISIBLE
        //}
        //}
        //timer.start()
    }
}


