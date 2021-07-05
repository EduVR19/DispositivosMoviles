package com.example.dispositivosmoviles

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
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
                    val docref = db.collection("Usuarios").document(email)
                    docref.get().addOnSuccessListener {
                        val user = it.toObject<UsuarioClass>()
                        if(user?.admin == false && user?.bloquear == false){
                            FirebaseAuth.getInstance().
                            signInWithEmailAndPassword(emailEditText.text.toString(),passwordEditText.text.toString())
                                .addOnCompleteListener{
                                    if (it.isSuccessful && Firebase.auth.currentUser?.isEmailVerified == true) {
                                        showClientHome(it.result?.user?.email ?: "")
                                        //sendSignInLink(it.result?.user?.email ?: "", actionCodeSettings {  })
                                        emailEditText.text.clear()
                                        passwordEditText.text.clear()
                                    } else {
                                        showAlert()
                                    }
                                }
                                .addOnFailureListener{
                                    showAlert2("Falló el sign up")
                                }
                        }
                        else
                        {
                            showAlert2("No eres usuario")
                        }
                    }
                        .addOnFailureListener{
                            showAlert2("Falló la colección de datos")
                        }
                } else
                {
                    val email = emailEditText.text.toString()
                    val docref = db.collection("Usuarios").document(email)
                    docref.get().addOnSuccessListener {
                        val user = it.toObject<UsuarioClass>()
                        if(user==null){
                            showAlert2("el user dio null")
                        }
                        if(user?.admin == true && user?.bloquear == false){
                            FirebaseAuth.getInstance().
                            signInWithEmailAndPassword(emailEditText.text.toString(),passwordEditText.text.toString())
                                .addOnCompleteListener{
                                    if (it.isSuccessful && Firebase.auth.currentUser?.isEmailVerified == true) {
                                        showAdminHome(it.result?.user?.email ?: "")
                                        //sendSignInLink(it.result?.user?.email ?: "", actionCodeSettings {  })
                                        emailEditText.text.clear()
                                        passwordEditText.text.clear()
                                    } else {
                                        showAlert()
                                    }
                                }
                                .addOnFailureListener{
                                    showAlert2("Falló el sign up")
                                }
                        }
                        else
                        {
                            showAlert2("No eres administrador")
                        }
                    }
                        .addOnFailureListener{
                            showAlert2("Falló la colección de datos")
                        }
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
    private fun showAdminHome(email: String) {

        val adminHomeIntent = Intent(this, MainAdminActivity::class.java).apply {
            putExtra("email", email)
            // putExtra("provider", provider.name)
        }
        startActivity(adminHomeIntent)
    }
    private fun showClientHome(email: String) {
        val clientHomeIntent = Intent(this, MainClientActivity::class.java).apply {
            putExtra("email", email)
            // putExtra("provider", provider.name)
        }
        startActivity(clientHomeIntent)
    }

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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finishAffinity();
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event)
    }

}
