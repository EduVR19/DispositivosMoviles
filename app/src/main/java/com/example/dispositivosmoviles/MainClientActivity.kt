package com.example.dispositivosmoviles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import com.example.dispositivosmoviles.activities.ListOfChatsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main_client.*

class MainClientActivity : AppCompatActivity() {
    //Chat
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_client)
        val bundle: Bundle? = intent.extras
        val email:String? = bundle?.getString("email")

        setup()
        loadTexts(email ?: "")


        //Chat
        chatButton.setOnClickListener { startChat() }

        // Reservación
        resbutton.setOnClickListener {
            val homeIntent = Intent(this, ReserveClientActivity::class.java).apply {
                putExtra("email", email)
                // putExtra("provider", provider.name)
            }
            startActivity(homeIntent)
        }

    }
    private fun startChat(){
        val currentUser = auth.currentUser

        if(currentUser != null){
            val intent = Intent(this, ListOfChatsActivity::class.java)
            intent.putExtra("user", currentUser.email)
            startActivity(intent)

            finish()
        }
    }

    private fun loadTexts(email: String){
        val db = FirebaseFirestore.getInstance()
        val docref = db.collection("Usuarios").document(email)

        docref.get()
            .addOnSuccessListener {
                val user = it.toObject<UsuarioClass>()

                nameClient.text = user?.nombre.toString()
                emailClient.text = user?.correo.toString()
                phoneClient.text = user?.telefono.toString()
                petNameclient.text = user?.mascota?.nombre
                petRaceClient.text = user?.mascota?.sexo
                petAgeClient.text = user?.mascota?.edad
            }
    }
    private fun setup( ) {
        title = "Cliente"
        revClientButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
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