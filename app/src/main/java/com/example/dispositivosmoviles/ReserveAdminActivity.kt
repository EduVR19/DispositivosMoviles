package com.example.dispositivosmoviles

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_reserve.*
import kotlinx.android.synthetic.main.activity_reserve.fechafeditt
import kotlinx.android.synthetic.main.activity_reserve.fechaieditt
import kotlinx.android.synthetic.main.activity_reserve.imageButton
import kotlinx.android.synthetic.main.activity_reserve_admin.*
import kotlinx.android.synthetic.main.activity_reserve_client.*

class ReserveAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve_admin)
        setup()

        imageButton.setOnClickListener {
            startActivity(Intent(this, ReservacionAdminActivity::class.java))
        }

    }
    private fun setup(){
        title ="Crear Reservacion"
        val db = FirebaseFirestore.getInstance()
        buttonreserve.setOnClickListener {
            if (fechafeditt.text.isNotEmpty() && fechaieditt.text.isNotEmpty() && clienteeditt.text.isNotEmpty()) {
                val clienteee = clienteeditt.text.toString()
                db.collection("Reservación").document(clienteee).get().addOnCompleteListener {
                    // Reservación
                    val diaf = fechafeditt.text.toString()
                    val diai = fechaieditt.text.toString()
                    val dias = fechaieditt.text.toString()
                    val not = notaseditt.text.toString()
                    var reservacion = Reservacion(diaf, diai, dias, not, "8")

                    db.collection("Reservacion").document(clienteee).set(reservacion)

                    showAlertEmail()
                }
            } else {
                showAlert()
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

    private fun showAlert()
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Le falta rellenar")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertEmail()
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Reservación exitosa")
        builder.setMessage("Ya genero la reservación")
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, id ->
            // Mandar de nuevo a la pantalla de login
            val loginIntent = Intent(this, ReservacionAdminActivity::class.java)
            startActivity(loginIntent)

        })
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }
}