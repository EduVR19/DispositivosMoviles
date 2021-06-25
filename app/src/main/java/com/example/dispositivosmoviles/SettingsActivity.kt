package com.example.dispositivosmoviles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_settings.*

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
    }



}
