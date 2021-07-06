package com.example.dispositivosmoviles

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.activity_crear_admin.*
import kotlinx.android.synthetic.main.activity_informacion_cliente_reservacion.*
import kotlinx.android.synthetic.main.activity_main_client.*

class CrearAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_admin)
        val bundle: Bundle? = intent.extras
        val email: String? = bundle?.getString("email")

        botonregresarbutton3.setOnClickListener {
            startActivity(Intent(this, MainAdminActivity::class.java))
        }

        val db = FirebaseFirestore.getInstance()
        val docref = db.collection("Usuarios").document(email.toString())

        docref.get()
            .addOnSuccessListener {
                val user = it.toObject<UsuarioClass>()

                nombrett.text = user?.nombre.toString()
                correott.text = user?.correo.toString()
                Telefonott.text = user?.telefono.toString()
            }

        Adminbuton.setOnClickListener {
            val telefono = Telefonott.text.toString()
            val nombre = nombrett.text.toString()
            val correo = correott.text.toString()

            //mascota
            val nombrePerro = ""
            val razaPerro = ""
            val edadperro = ""
            var mascotaNueva = Mascota(nombrePerro, razaPerro, edadperro)

            var usuarioNuevo = UsuarioClass(correo, nombre, telefono, true, false, mascotaNueva)
            showAlertEmail()
            db.collection("Usuarios").document(email.toString()).set(usuarioNuevo)
        }

    }

    private fun showAlertEmail() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Registro exitoso")
        builder.setMessage("Cambio la informacion")
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, id ->
            // Mandar de nuevo a la pantalla de login
            val loginIntent = Intent(this, MainAdminActivity::class.java)
            startActivity(loginIntent)

        })
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }
}

