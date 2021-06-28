        package com.example.dispositivosmoviles

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        // Setup
        setup()
        languages()

    }

    private fun setup() {
        title = "Login"

        val db = FirebaseFirestore.getInstance()
        val kindusers = findViewById<Spinner>(R.id.Usuariokinds)
        val usuarioslist = resources.getStringArray(R.array.usuarioList)
        // val usuarioslist = listOf("Administrador", "Usuario")
        val adaptadorlist = ArrayAdapter(this, android.R.layout.simple_spinner_item, usuarioslist)

        kindusers.adapter = adaptadorlist

        loginButton.setOnClickListener {
                if (kindusers.getItemAtPosition(kindusers.selectedItemPosition).toString() == "Usuario" || kindusers.getItemAtPosition(kindusers.selectedItemPosition).toString() == "User" ) {
                    val email = emailEditText.text.toString()

                    db.collection("Usuarios").document(email).get()
                        .addOnSuccessListener {
                            val isAdmin = it.get("isAdmin");
                            if(isAdmin == false){
                                FirebaseAuth.getInstance().
                                            signInWithEmailAndPassword(emailEditText.text.toString(),
                                                                       passwordEditText.text.toString())
                                            .addOnCompleteListener{
                                                if (it.isSuccessful && Firebase.auth.currentUser?.isEmailVerified == true) {
                                                    showHome(it.result?.user?.email ?: "")
                                                    //sendSignInLink(it.result?.user?.email ?: "", actionCodeSettings {  })
                                                    emailEditText.text.clear()
                                                    passwordEditText.text.clear()
                                                } else {
                                                    showAlert()
                                                }
                                            }
                            }else
                            {
                                showAlert2("No eres usuario")
                            }
                        }

                    /*db.collection("Usuario").document(email).get()
                        .addOnSuccessListener {
                            val contrasenaDocumento = it.get("Contraseña")
                            if(contrasenaDocumento != null){
                                if (passwordEditText.text.toString() == contrasenaDocumento) {
                                    FirebaseAuth.getInstance().signInWithEmailAndPassword(
                                        emailEditText.text.toString(),
                                        passwordEditText.text.toString()

                                    ).addOnCompleteListener {
                                        if (it.isSuccessful && Firebase.auth.currentUser?.isEmailVerified == true) {
                                            showHome(it.result?.user?.email ?: "")
                                            //sendSignInLink(it.result?.user?.email ?: "", actionCodeSettings {  })
                                            emailEditText.text.clear()
                                            passwordEditText.text.clear()
                                        } else {
                                            showAlert()
                                        }
                                    }
                                }
                            } else{
                                showAlert()
                            }


                        }
                        .addOnFailureListener{
                            showAlert()
                        }*/
                } else
                {
                    val email = emailEditText.text.toString()

                    db.collection("Usuarios").document(email).get()
                        .addOnSuccessListener {
                            val isAdmin = it.get("isAdmin");
                            if(isAdmin == true){
                                FirebaseAuth.getInstance().
                                signInWithEmailAndPassword(emailEditText.text.toString(),
                                    passwordEditText.text.toString())
                                    .addOnCompleteListener{
                                        if (it.isSuccessful && Firebase.auth.currentUser?.isEmailVerified == true) {
                                            showHome(it.result?.user?.email ?: "")
                                            //sendSignInLink(it.result?.user?.email ?: "", actionCodeSettings {  })
                                            emailEditText.text.clear()
                                            passwordEditText.text.clear()
                                        } else {
                                            showAlert()
                                        }
                                    }
                            }
                            else
                            {
                                showAlert2("No eres administrador")
                            }
                        }
                    /*
                    val email = emailEditText.text.toString()

                    db.collection("Administrador").document(email).get()
                        .addOnSuccessListener {
                            val contrasenaDocumento = it.get("Contraseña")
                            if(contrasenaDocumento != null){
                                if (passwordEditText.text.toString() == contrasenaDocumento) {
                                    FirebaseAuth.getInstance().signInWithEmailAndPassword(
                                        emailEditText.text.toString(),
                                        passwordEditText.text.toString()

                                    ).addOnCompleteListener {
                                        if (it.isSuccessful && Firebase.auth.currentUser?.isEmailVerified == true) {
                                            showHome(it.result?.user?.email ?: "")
                                            //sendSignInLink(it.result?.user?.email ?: "", actionCodeSettings {  })
                                            emailEditText.text.clear()
                                            passwordEditText.text.clear()
                                        } else {
                                            showAlert()
                                        }
                                    }
                                }
                            } else{
                                showAlert()
                            }


                        }
                        .addOnFailureListener{
                            showAlert()
                        }
                    */
                }



        }

        registrarText.setOnClickListener {
            val signUpIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signUpIntent)
        }

        contrasenaOlvidada.setOnClickListener {
            val contrasenaOlvidadaIntent = Intent(this, RestablecerActivity::class.java)
            startActivity(contrasenaOlvidadaIntent)
        }

    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error")
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


    private fun showHome(email: String) {

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
    lateinit var MyPreference: MyPreference
    override fun attachBaseContext(newBase: Context?) {
        MyPreference = MyPreference(newBase!!)
        val lang = MyPreference.getLoginCount()

        super.attachBaseContext(MyContextWrapper.wrap(newBase, lang))

    }

    private fun languages() {
        textViewLanguage.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

   /* private fun tipodeuser() {
        val kindusers = findViewById<Spinner>(R.id.Usuariokinds)
        val usuarioslist = resources.getStringArray(R.array.usuarioList)
        // val usuarioslist = listOf("Administrador", "Usuario")
        val adaptadorlist = ArrayAdapter(this, android.R.layout.simple_spinner_item, usuarioslist)
        kindusers.adapter = adaptadorlist

        kindusers.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val posicion = usuarioslist[position].toInt()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }*/

}
