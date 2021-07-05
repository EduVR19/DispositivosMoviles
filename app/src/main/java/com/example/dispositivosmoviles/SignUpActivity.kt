package com.example.dispositivosmoviles

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_auth.emailEditText
import kotlinx.android.synthetic.main.activity_auth.passwordEditText
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        //SETUP
        setup()
    }
    private fun setup() {
        title = "Registro"
        val db = FirebaseFirestore.getInstance()

        signUpButton.setOnClickListener {
            if (passwordEditText.text.toString() == editTextPassword.text.toString()){

                if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()) {
                    val email = emailEditText.text.toString()
                    val contrasena = passwordEditText.text.toString()
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,contrasena)
                        .addOnCompleteListener {
                        if (it.isSuccessful) {
                            //usuario
                            val telefono = telefonotextview.text.toString()
                            val nombre = nametextview.text.toString()

                            //mascota
                            val nombrePerro = mascotatextview.text.toString()
                            val razaPerro = razatextview.text.toString()
                            val edadperro = edadmtextview.text.toString()
                            var mascotaNueva = Mascota(nombrePerro,razaPerro,edadperro)

                            var usuarioNuevo = UsuarioClass(email,nombre,telefono,false, false,mascotaNueva)

                            //showHome(it.result?.user?.email ?: "")
                            sendEmailVerification()
                            showAlertEmail()
                            db.collection("Usuarios").document(email).set(usuarioNuevo)

                        } else {
                            showAlert2("No se agregó a la bd")
                        }
                    }
                }
            }
            else {
                showAlert2("Las contraseñas no coinciden")
            }
        }
        imageButton4.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
        }
    }


    private fun showAlert()
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("La contraseña no coincide")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    private fun showAlert2( mensaje: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertEmail()
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Registro exitoso")
        builder.setMessage("Por favor revisa tu correo para la autenticación")
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, id ->
            // Mandar de nuevo a la pantalla de login
            val loginIntent = Intent(this, AuthActivity::class.java)
            startActivity(loginIntent)

        })
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    private fun sendEmailVerification() {
        // [START send_email_verification]
        val user = Firebase.auth.currentUser

        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "Email sent.")
                }
            }
        // [END send_email_verification]
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finishAffinity();
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event)
    }
}