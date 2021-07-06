package com.example.dispositivosmoviles

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_reserve_admin.*

class ReserveAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve_admin)
        setup()

        imageButton.setOnClickListener {
            startActivity(Intent(this, ReservacionAdminActivity::class.java))
        }

        fechainicioedit.setOnClickListener { showDatePickerDialog() }
        fechafin.setOnClickListener { showDatePickerDialog2() }

    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment {
                day, month, year -> onDateSelected(day, month + 1, year)
        }

        datePicker.show(supportFragmentManager, "datePicker")
    }

    fun onDateSelected(day:Int, month:Int, year:Int){
        fechainicioedit.setText("$day/$month/$year")
    }

    private fun showDatePickerDialog2() {
        val datePicker = DatePickerFragment {
                day, month, year -> onDateSelected2(day, month + 1, year)
        }

        datePicker.show(supportFragmentManager, "datePicker")
    }

    fun onDateSelected2(day:Int, month:Int, year:Int){
        fechafin.setText("$day/$month/$year")
    }


    private fun setup(){
        title ="Crear Reservacion"
        val db = FirebaseFirestore.getInstance()
        buttonreserve.setOnClickListener {
            if (fechafin.text.isNotEmpty() && clienteeditt.text.isNotEmpty()) {
                val clienteee = clienteeditt.text.toString()
                db.collection("Reservacion").document(clienteee).get().addOnCompleteListener {
                    // Reservación
                    val cor = clienteeditt.text.toString()
                    val diaf = fechafin.text.toString()
                    val diai = fechainicioedit.text.toString()

                    val not = notaseditt.text.toString()
                    var reservacion = ReservacionClassClass(cor,diaf, diai, not,"8", false, false)

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