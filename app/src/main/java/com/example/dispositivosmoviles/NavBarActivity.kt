package com.example.dispositivosmoviles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_reserve_admin.*

class NavBarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_bar)

        imageButton.setOnClickListener {
            val imageButtonIntent = Intent(this, AuthActivity::class.java)
            startActivity(imageButtonIntent)
        }

    }
}