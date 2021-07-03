package com.example.dispositivosmoviles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_listadereser_admin.*
import kotlinx.android.synthetic.main.activity_reservacion_admin.*

class ListadereserAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listadereser_admin)

        setup()

        Botonregresarbutton.setOnClickListener {
            startActivity(Intent(this, ReservacionAdminActivity::class.java))
        }
    }

    private fun setup(){
        val db = FirebaseFirestore.getInstance()
        
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finishAffinity();
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event)
    }
}