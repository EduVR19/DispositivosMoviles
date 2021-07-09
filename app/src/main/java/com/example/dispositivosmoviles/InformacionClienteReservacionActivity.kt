package com.example.dispositivosmoviles

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_informacion_cliente_reservacion.*
import kotlinx.android.synthetic.main.activity_main_admin.*
import kotlinx.android.synthetic.main.activity_main_client.*
import kotlinx.android.synthetic.main.activity_reserve_client.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.emailEditText

class InformacionClienteReservacionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informacion_cliente_reservacion)
        val bundle: Bundle? = intent.extras
        val email:String? = bundle?.getString("email")

        botonregresarbutton2.setOnClickListener {
            startActivity(Intent(this, ListadereserAdminActivity::class.java))
        }

        info(email ?: "")

        button7.setOnClickListener {
            if (codigo.text.isNotEmpty()) {
                val db = FirebaseFirestore.getInstance()
                val d = db.collection("Reservacion").document(email.toString()).collection("Reserve").document(codigo.text.toString())
                d.get().addOnSuccessListener {
                    val user = it.toObject<ReservacionClassClass>()
                    if (user != null) {
                        val homeIn =
                            Intent(this, ReservacionCambioAdminActivity::class.java).apply {
                                putExtra("email", email)
                                putExtra("codigo", codigo.text.toString())
                            }
                        startActivity(homeIn)
                        // putExtra("provider", provider.name)
                    } else {
                        showAlertReservacion()
                    }
                }
            }
            else {
                showAlertReservacion()
            }
        }

        bloquearbutton.setOnClickListener { block(email ?: "") }
    }
    private fun info(email: String){
        val db = FirebaseFirestore.getInstance()
        val doc = db.collection("Usuarios").document(email)
        doc.get()
            .addOnSuccessListener {
                val user = it.toObject<UsuarioClass>()
                textView31.text = user?.nombre.toString()
                textView32.text = user?.telefono.toString()
                textView34.text = user?.mascota?.nombre
                textView35.text = user?.mascota?.sexo
                textView36.text = user?.mascota?.edad
            }
        button3uuu.setOnClickListener {
            if (Nombrecliente.text.isNotEmpty() && Telefonoclientee.text.isNotEmpty() && mosta.text.isNotEmpty() && edadmosa.text.isNotEmpty() && razamaso.text.isNotEmpty()){
                val telefono = Telefonoclientee.text.toString()
                val nombre = Nombrecliente.text.toString()

                //mascota
                val nombrePerro = mosta.text.toString()
                val razaPerro = razamaso.text.toString()
                val edadperro = edadmosa.text.toString()
                var mascotaNueva = Mascota(nombrePerro,razaPerro,edadperro)

                var usuarioNuevo = UsuarioClass(email,nombre,telefono,false, false,mascotaNueva)
                showAlertEmail()
                db.collection("Usuarios").document(email).set(usuarioNuevo)
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
    private fun showAlertReservacion(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("No tiene reservacion")
        builder.setPositiveButton("Aceptar", null)
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

    private fun block(email: String){
        val db = FirebaseFirestore.getInstance()
        val telefono = ""
        val nombre = ""

        //mascota
        val nombrePerro = ""
        val razaPerro = ""
        val edadperro = ""
        var mascotaNueva = Mascota(nombrePerro,razaPerro,edadperro)

        var usuarioNuevo = UsuarioClass(email,nombre,telefono,false, true,mascotaNueva)
        showAlertEmail()
        db.collection("Usuarios").document(email).set(usuarioNuevo)
        db.collection("Reservacion").document(email)
            .delete()
            .addOnSuccessListener {  }
            .addOnFailureListener {  }
    }

}