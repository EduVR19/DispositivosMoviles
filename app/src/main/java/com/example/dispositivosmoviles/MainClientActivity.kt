package com.example.dispositivosmoviles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.activity_main_client.*

class MainClientActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_client)
        val bundle: Bundle? = intent.extras
        val email:String? = bundle?.getString("email")

        setup()
        loadTexts(email ?: "")


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
            }
    }
    private fun setup( ) {
        title = "Cliente"
        button11.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
    }
}