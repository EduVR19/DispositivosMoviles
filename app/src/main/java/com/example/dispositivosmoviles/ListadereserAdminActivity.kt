package com.example.dispositivosmoviles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_auth.passwordEditText
import kotlinx.android.synthetic.main.activity_list_of_reserves.*
import kotlinx.android.synthetic.main.activity_main_admin.*
import kotlinx.android.synthetic.main.activity_sign_up.*


class ListadereserAdminActivity : AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var userArrayList : ArrayList<User>
    private lateinit var myAdapter: MyAdapter
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_reserves)

        recyclerView =  findViewById(R.id.recycleView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        userArrayList = arrayListOf()

        myAdapter = MyAdapter(userArrayList)

        recyclerView.adapter = myAdapter

        EventChangeListener()

        Botonregresarbutton.setOnClickListener {
            startActivity(Intent(this, ReservacionAdminActivity::class.java))
        }

        cambiarbutton.setOnClickListener {
            cambiarinfo()
        }

    }

    
    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("Reservacion")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    db.collection("Reservacion").document(document.id).collection("Reserve").
                    addSnapshotListener(object : EventListener<QuerySnapshot>{
                        override fun onEvent(
                            value: QuerySnapshot?,
                            error: FirebaseFirestoreException?
                        ) {
                            if (error != null){
                                Log.e("Firestore Error", error.message.toString())
                                return
                            }
                            for (dc : DocumentChange in value?.documentChanges!!){
                                if (dc.type == DocumentChange.Type.ADDED){
                                    userArrayList.add(dc.document.toObject(User::class.java))
                                }
                            }

                            myAdapter.notifyDataSetChanged()
                        }
                    })
                }
            }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finishAffinity();
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun cambiarinfo() {
        val db = FirebaseFirestore.getInstance()
        val email = edittextuser.text.toString()
        if (edittextuser.text.isNotEmpty()) {
            val docref = db.collection("Usuarios").document(email)
            docref.get()
                .addOnSuccessListener {
                    val user = it.toObject<UsuarioClass>()
                    if (user != null) {
                        val homeIntent = Intent(this, InformacionClienteReservacionActivity::class.java).apply {
                                putExtra("email", email)
                                // putExtra("provider", provider.name)
                            }
                        startActivity(homeIntent)
                    } else {
                        showAlert2()
                    }
                }
                .addOnFailureListener {
                    showAlert2()
                }
        }
        else{
            showAlert()
        }
    }
    private fun showAlert()
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Debe seleccionar un usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlert2()
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("El cliente no existe")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


}
