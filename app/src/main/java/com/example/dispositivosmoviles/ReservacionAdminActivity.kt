package com.example.dispositivosmoviles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_reservacion_admin.*

class ReservacionAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservacion_admin)

        button5.setOnClickListener {
            startActivity(Intent(this, ReserveAdminActivity::class.java))
        }

        imageButton3.setOnClickListener {
            startActivity(Intent(this, MainAdminActivity::class.java))
        }

        button6.setOnClickListener {
            startActivity(Intent(this, MainAdminActivity::class.java))
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