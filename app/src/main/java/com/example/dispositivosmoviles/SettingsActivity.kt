package com.example.dispositivosmoviles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class SettingsActivity : AppCompatActivity() {
    lateinit var myPreference: MyPreference;
    val languageList = arrayOf("es","en");
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        myPreference = MyPreference(this)

        spinner.adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,languageList)

        val lang = myPreference.getLoginCount()
        val index = languageList.indexOf(lang);
        if(index>=0){
            spinner.setSelection(index)
        }
        button.setOnClickListener{
            myPreference.setLoginCount(languageList[spinner.selectedItemPosition])
            startActivity(Intent(this,AuthActivity::class.java))
            finish();
        }
        imageButton2.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
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
