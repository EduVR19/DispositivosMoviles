package com.example.dispositivosmoviles

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AlertDialog
import androidx.core.util.rangeTo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.activity_informacion_cliente_reservacion.*
import kotlinx.android.synthetic.main.activity_reservacion_cambio_admin.*

class ReservacionCambioAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservacion_cambio_admin)
        val bundle: Bundle? = intent.extras
        val email:String? = bundle?.getString("email")
        val codigo:String? = bundle?.getString("codigo")

        imageButton9.setOnClickListener {
            val ho = Intent(this, InformacionClienteReservacionActivity::class.java).apply {
                putExtra("email", email)
                // putExtra("provider", provider.name)
            }
            startActivity(ho)
        }

        infrese(email ?: "", codigo ?: "")

        button3.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            db.collection("Reservacion").document(email.toString()).collection("Reserve").document(codigo.toString())
                .delete()
                .addOnSuccessListener { showAlertEmail2() }
                .addOnFailureListener {  }
        }


        //Date picker
        fechainieditt.setOnClickListener { showDatePickerDialog() }
        fechafineditt.setOnClickListener { showDatePickerDialog2() }
        //Fin Date picker

    }


    //Date picker
    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment {
                day, month, year -> onDateSelected(day, month + 1, year)
        }

        datePicker.show(supportFragmentManager, "datePicker")
    }

    fun onDateSelected(day:Int, month:Int, year:Int){
        fechainieditt.setText("$day/$month/$year")
    }

    private fun showDatePickerDialog2() {
        val datePicker = DatePickerFragment {
                day, month, year -> onDateSelected2(day, month + 1, year)
        }

        datePicker.show(supportFragmentManager, "datePicker")
    }

    fun onDateSelected2(day:Int, month:Int, year:Int){
        fechafineditt.setText("$day/$month/$year")
    }
    //fin date picker




    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finishAffinity();
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event)
    }
    private fun infrese(email: String, codigo: String){
        val db = FirebaseFirestore.getInstance()
        val doc = db.collection("Reservacion").document(email).collection("Reserve").document(codigo)
        doc.get()
            .addOnSuccessListener {
                val user = it.toObject<ReservacionClassClass>()
                gmail.text = user?.correo.toString()
                fechaini.text = user?.fechainicio.toString()
                fechafinn.text = user?.fechafin.toString()
                notaas.text = user?.nota.toString()
                if (user?.checkin == true){
                    switch1.isChecked = true
                }

                if (user?.checkout == true){
                    switch2.isChecked = true
                }
            }

        button3uuu2.setOnClickListener {
            if (fechainieditt.text.isNotEmpty() && notasedditt.text.isNotEmpty()){
                val fechafin =  fechafineditt.text.toString()
                val fechainicio = fechainieditt.text.toString()
                val notas = notasedditt.text.toString()
                val checkin = switch1.isChecked
                val checkout = switch2.isChecked

                var usuarioNuevo = ReservacionClassClass(email, fechafin, fechainicio, notas, "8", checkin, checkout,codigo)
                showAlertEmail()
                db.collection("Reservacion").document(email).collection("Reserve").document(codigo).set(usuarioNuevo)
            }
            else{
                showAlert()
            }
        }
    }
    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Falta rellenar")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertEmail()
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Registro exitoso")
        builder.setMessage("Cambio la informacion")
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, id ->
            // Mandar de nuevo a la pantalla de login
            val loginIntent = Intent(this, ListadereserAdminActivity::class.java)
            startActivity(loginIntent)

        })
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }
    private fun showAlertEmail2()
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Registro exitoso")
        builder.setMessage("Cambio la informacion")
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, id ->
            // Mandar de nuevo a la pantalla de login
            val loginIntent = Intent(this, ListadereserAdminActivity::class.java)
            startActivity(loginIntent)

        })
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }
}