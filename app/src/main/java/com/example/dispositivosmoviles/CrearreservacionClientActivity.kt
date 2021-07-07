package com.example.dispositivosmoviles

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.activity_crearreservacion_client.*

class CrearreservacionClientActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crearreservacion_client)
        val bundle: Bundle? = intent.extras
        val email: String? = bundle?.getString("email")

        setup()
        crearreseve(email ?: "")
        ggg(email ?: "")

        fechainicioedit2.setOnClickListener { showDatePickerDialog() }
        fechafin2.setOnClickListener { showDatePickerDialog2() }

    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment {
                day, month, year -> onDateSelected(day, month + 1, year)
        }

        datePicker.show(supportFragmentManager, "datePicker")
    }

    fun onDateSelected(day:Int, month:Int, year:Int){
        fechainicioedit2.setText("$day/$month/$year")
    }

    private fun showDatePickerDialog2() {
        val datePicker = DatePickerFragment {
                day, month, year -> onDateSelected2(day, month + 1, year)
        }

        datePicker.show(supportFragmentManager, "datePicker")
    }

    fun onDateSelected2(day:Int, month:Int, year:Int){
        fechafin2.setText("$day/$month/$year")
    }

    private fun crearreseve(email: String){
        val db = FirebaseFirestore.getInstance()
        val dc = db.collection("Usuarios").document(email)
        dc.get()
            .addOnSuccessListener {
                val yipos = it.toObject<UsuarioClass>()
                clienteeditt2.text = yipos?.correo.toString()
            }

    }
    private fun ggg(email: String){
        val db = FirebaseFirestore.getInstance()
        buttonreserve2.setOnClickListener {
            if (fechafin2.text.isNotEmpty() && fechainicioedit2.text.isNotEmpty()) {
                db.collection("Reservacion").document(email).get().addOnCompleteListener {
                    // Reservación
                    val corr = email
                    val diaf = fechafin2.text.toString()
                    val diai = fechainicioedit2.text.toString()
                    val not = notaseditt2.text.toString()
                    var reservacion = ReservacionClassClass(corr,diaf, diai, not, "8", false, false)

                    db.collection("Reservacion").document(email).set(reservacion)

                    showAlertEmail()
                }
            } else {
                showAlert()
            }
        }
    }

    private fun setup( ) {
        title = "Cliente"
        val bundle: Bundle? = intent.extras
        val email:String? = bundle?.getString("email")
        regre.setOnClickListener {
            val homeIn = Intent(this, ReserveClientActivity::class.java).apply {
                putExtra("email", email)
                // putExtra("provider", provider.name)
            }
            startActivity(homeIn)
        }
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
        val bundle: Bundle? = intent.extras
        val email:String? = bundle?.getString("email")
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, id ->
            val homeIn = Intent(this, MainClientActivity::class.java).apply {
                putExtra("email", email)
                // putExtra("provider", provider.name)
            }
            startActivity(homeIn)

        })
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finishAffinity();
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event)
    }
}