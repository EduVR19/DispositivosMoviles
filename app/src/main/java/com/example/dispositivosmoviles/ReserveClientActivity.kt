package com.example.dispositivosmoviles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_reserve_client.*

class ReserveClientActivity : AppCompatActivity() {

    //Lista de reservaciones
    private lateinit var recyclerView : RecyclerView
    private lateinit var userArrayList : ArrayList<Reserve>
    private lateinit var myAdapter: MyAdapter2
    private lateinit var db : FirebaseFirestore
    //Lista de reservaciones


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve_client)
        val bundle: Bundle? = intent.extras
        val email: String? = bundle?.getString("email")
        setup()
        //loadTexts(email ?: "")

        //Lista de reservaciones
        recyclerView =  findViewById(R.id.recycleViewReserve)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        userArrayList = arrayListOf()

        myAdapter = MyAdapter2(userArrayList)

        recyclerView.adapter = myAdapter

        EventChangeListener(email ?: "")
        //Lista de reservaciones



    }

    private fun EventChangeListener(email: String) {
        db = FirebaseFirestore.getInstance()

        val userRef = db.collection("Reservacion").document(email)

        userRef.collection("Reserve").
        addSnapshotListener(object : EventListener<QuerySnapshot> {
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
                        userArrayList.add(dc.document.toObject(Reserve::class.java))
                    }
                }

                myAdapter.notifyDataSetChanged()
            }
        })
    }



    /*
    private fun loadTexts(email: String){
        val db = FirebaseFirestore.getInstance()
        val doc = db.collection("Reservacion").document(email)

        doc.get()
            .addOnSuccessListener {
                val yipos = it.toObject<ReservacionClassClass>()
                fechainicioview.text = yipos?.fechainicio.toString()
                fechafinviw.text = yipos?.fechafin.toString()
                ffff.text = yipos?.hora.toString()
                horaentrada2.text = yipos?.hora.toString()
            }
    }
    */
    private fun setup( ) {
        title = "Cliente"
        val bundle: Bundle? = intent.extras
        val email: String? = bundle?.getString("email")
        val db = FirebaseFirestore.getInstance()

        holahola.setOnClickListener {
            val homeIn = Intent(this, MainClientActivity::class.java).apply {
                putExtra("email", email)
                // putExtra("provider", provider.name)
            }
            startActivity(homeIn)
        }
        buttondos.setOnClickListener {
            val d = db.collection("Reservacion").document(email.toString())
            d.get().addOnSuccessListener {
                    val homeIn = Intent(this, CrearreservacionClientActivity::class.java).apply {
                        putExtra("email", email)
                    }
                    startActivity(homeIn)
                    // putExtra("provider", provider.name)
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

}
