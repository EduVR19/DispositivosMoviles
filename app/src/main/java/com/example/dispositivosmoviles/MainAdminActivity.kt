package com.example.dispositivosmoviles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_main_admin.*

class MainAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_admin)
        val bundle: Bundle? = intent.extras
        val email:String? = bundle?.getString("email")

        setup(email ?: "")
    }

    private fun setup(email: String/*, provider: String)*/ ) {
        val db = FirebaseFirestore.getInstance()
        title = "Inicio"


        logOutAdmin.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

        button2.setOnClickListener {
            startActivity(Intent(this, ReservacionAdminActivity::class.java))
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