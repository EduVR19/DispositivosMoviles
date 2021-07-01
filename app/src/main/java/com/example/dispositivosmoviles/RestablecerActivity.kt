package com.example.dispositivosmoviles

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_auth.emailEditText
import kotlinx.android.synthetic.main.activity_auth.passwordEditText
import kotlinx.android.synthetic.main.activity_restablecer.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class RestablecerActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var txtEmail:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restablecer)

        txtEmail = findViewById(R.id.emailRestablecerEdtiText)
        auth = FirebaseAuth.getInstance()

        imageButton5.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
        }
    }

    fun send(View:View){
        val email = emailRestablecerEdtiText.text.toString()

        if(!TextUtils.isEmpty(email)) {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener (this) {
                    task ->
                    if (task.isSuccessful){
                        showAlertEmail()
                    }
                    else
                    {
                        showAlert()
                    }
                }
        }
    }

    private fun showAlert()
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("El correo ingresado no está registrado")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertEmail()
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Envío exitoso")
        builder.setMessage("Por favor revisa tu correo para el restablecimiento")
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, id ->
            // Mandar de nuevo a la pantalla de login
            val loginIntent = Intent(this, AuthActivity::class.java)
            startActivity(loginIntent)

        })
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    private fun sendPasswordReset() {
        // [START send_password_reset]
        val db = FirebaseFirestore.getInstance()
        val emailAddress = "user@example.com"

        Firebase.auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")

                }
            }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finishAffinity();
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event)
    }
}