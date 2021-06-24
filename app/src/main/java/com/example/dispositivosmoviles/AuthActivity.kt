package com.example.dispositivosmoviles

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // Setup
        setup()
    }

    private fun setup() {
        title = "Autenticación"

        signUpButton.setOnClickListener {
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty() )
            {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString()).addOnCompleteListener {


                    if (it.isSuccessful)
                    {
                        //showHome(it.result?.user?.email ?: "")
                        sendEmailVerification()
                        showAlertEmail()
                    }
                    else
                    {
                        showAlert()
                    }
                }



            }
        }

        loginButton.setOnClickListener {
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty() )
            {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful && Firebase.auth.currentUser?.isEmailVerified == true)
                    {
                        showHome(it.result?.user?.email ?: "")
                        //sendSignInLink(it.result?.user?.email ?: "", actionCodeSettings {  })
                    }
                    else
                    {
                        showAlert()
                    }
                }
            }
        }
    }

    private fun showAlert()
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertEmail()
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Registro exitoso")
        builder.setMessage("Por favor revisa tu correo para la autenticación")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String)
    {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
            // putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }

 /*   private fun buildActionCodeSettings() {
        // [START auth_build_action_code_settings]
        val actionCodeSettings = actionCodeSettings {
            // URL you want to redirect back to. The domain (www.example.com) for this
            // URL must be whitelisted in the Firebase Console.
            url = "https://www.example.com/finishSignUp?cartId=1234"
            // This must be true
            handleCodeInApp = true
            setIOSBundleId("com.example.ios")
            setAndroidPackageName(
                "com.example.android",
                true, /* installIfNotAvailable */
                "12" /* minimumVersion */)
        }
        // [END auth_build_action_code_settings]
    }

    private fun sendSignInLink(email: String, actionCodeSettings: ActionCodeSettings) {
        // [START auth_send_sign_in_link]
        Firebase.auth.sendSignInLinkToEmail(email, actionCodeSettings)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                }
            }
        // [END auth_send_sign_in_link]
    } */


    private fun sendEmailVerification() {
        // [START send_email_verification]
        val user = Firebase.auth.currentUser

        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                }
            }
        // [END send_email_verification]
    }
}