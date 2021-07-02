package com.example.dispositivosmoviles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_main_client.*
import kotlinx.android.synthetic.main.activity_reserve_admin.*
import kotlinx.android.synthetic.main.activity_reserve_client.*
import kotlinx.android.synthetic.main.activity_reserve_client.fechafeditt
import kotlinx.android.synthetic.main.activity_reserve_client.fechaieditt

class ReserveClientActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve_client)



        imageButton9.setOnClickListener {
            startActivity(Intent(this, MainClientActivity::class.java))
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