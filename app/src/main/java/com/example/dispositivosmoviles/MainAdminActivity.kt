package com.example.dispositivosmoviles

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AlertDialog
import com.example.dispositivosmoviles.activities.ListOfChatsActivity
import com.example.dispositivosmoviles.activities.ListOfChatsAdminActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_informacion_cliente_reservacion.*
import kotlinx.android.synthetic.main.activity_list_of_reserves.*
import kotlinx.android.synthetic.main.activity_main_admin.*
import kotlinx.android.synthetic.main.activity_main_client.*

class MainAdminActivity : AppCompatActivity() {

    private val auth = Firebase.auth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_admin)
        val bundle: Bundle? = intent.extras
        val email:String? = bundle?.getString("email")

        setup(email ?: "")

        chatButtonAdmin.setOnClickListener { startChat() }



    }

    private fun startChat(){
        val currentUser = auth.currentUser

        if(currentUser != null){
            val intent = Intent(this, ListOfChatsAdminActivity::class.java)
            intent.putExtra("user", currentUser.email)
            startActivity(intent)

            finish()
        }
    }

    private fun setup(email: String/*, provider: String)*/ ) {
        val db = FirebaseFirestore.getInstance()
        title = "Inicio"


        logOutAdmin.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, AuthActivity::class.java))
        }

        button2.setOnClickListener {
            startActivity(Intent(this, ReservacionAdminActivity::class.java))
        }

        crearadminbuton.setOnClickListener {
            crearadmin()
        }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finishAffinity();
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun showAlert()
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Debe seleccionar un usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun crearadmin() {
        val db = FirebaseFirestore.getInstance()
        val email = admincorreo.text.toString()
        if (admincorreo.text.isNotEmpty()) {
            val docref = db.collection("Usuarios").document(email)
            docref.get()
                .addOnSuccessListener {
                    val user = it.toObject<UsuarioClass>()
                    if (user != null) {
                        val homeIntent = Intent(this, CrearAdminActivity::class.java).apply {
                            putExtra("email", email)
                            // putExtra("provider", provider.name)
                        }
                        startActivity(homeIntent)
                    } else {
                        showAlert2()
                    }
                }
                .addOnFailureListener {
                    showAlert2()
                }
        }
        else{
            showAlert()
        }
    }
    private fun showAlert2()
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("No se ha registrado")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}