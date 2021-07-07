package com.example.dispositivosmoviles.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dispositivosmoviles.adapters.MessageAdapter
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.UsuarioClass
import com.example.dispositivosmoviles.models.Message
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_chat.*

class   ChatActivity : AppCompatActivity() {
    private var chatId = ""
    private var user = ""
    private val fileResult = 1
    private var db = Firebase.firestore
    private var path : String =""
    private var imagenes : Int = 0
    private var imgUri : Uri = Uri.parse("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        intent.getStringExtra("chatId")?.let { chatId = it }
        intent.getStringExtra("user")?.let { user = it }

        if(chatId.isNotEmpty() && user.isNotEmpty()) {
            initViews()
        }
        galeriaButton()
        backButtonChat.setOnClickListener{
            super.onBackPressed()
        }
    }
    private fun galeriaButton(){
        galeriaButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2){
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
            }
            intent.type ="*/*"
            startActivityForResult(intent,fileResult)
        }
    }
    private fun initViews(){
        messagesRecylerView.layoutManager = LinearLayoutManager(this)
        messagesRecylerView.adapter = MessageAdapter(user)

        sendMessageButton.setOnClickListener { sendMessage() }

        val chatRef = db.collection("chats").document(chatId)

        chatRef.collection("messages").orderBy("dob", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { messages ->
                val listMessages = messages.toObjects(Message::class.java)
                (messagesRecylerView.adapter as MessageAdapter).setData(listMessages)
            }

        chatRef.collection("messages").orderBy("dob", Query.Direction.ASCENDING)
            .addSnapshotListener { messages, error ->
                if(error == null){
                    messages?.let {
                        val listMessages = it.toObjects(Message::class.java)
                        (messagesRecylerView.adapter as MessageAdapter).setData(listMessages)
                    }
                }
            }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == fileResult){
            if(resultCode == RESULT_OK && data!=null){
                val clipData = data.clipData
                if(clipData!= null){

                } else{
                    val uri = data.data
                    uri?.let { fileUpload(it)}
                }
            }
        }
    }
    private fun showAlert2( mensaje: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    private fun fileUpload(mUri: Uri) {

        val db = FirebaseFirestore.getInstance()
        val docref = db.collection("Usuarios").document(user)


        docref.get()
            .addOnSuccessListener {
                val user2 = it.toObject<UsuarioClass>()

                imagenes = user2!!.imagenes
        }
        imagenes++
        docref.update("imagenes",imagenes)

        val ref = FirebaseStorage.getInstance().reference
        val folder : StorageReference = FirebaseStorage.getInstance().reference.child("${user}/${imagenes}.jpg")
        folder.putFile(mUri)
            .addOnSuccessListener {
                showAlert2("Sí se subio, checa firebase")

                ref.child("${user}/${imagenes}.jpg").downloadUrl
                    .addOnSuccessListener {
                        path= it.toString()
                        sendMessage()
                        showAlert2("Sí consigiguio el downloarURl y todo")
                    }
                    .addOnFailureListener{
                        showAlert2("Falló el conseguir el downloadUrl")
                        path=""
                    }
            }
            .addOnFailureListener{
                showAlert2("Falló al subir a firebase")
            }


    }

    private fun sendMessage(){
        val message = Message(
            message = messageTextField.text.toString(),
            from = user,
            imgUrl = path,
        )


        db.collection("chats").document(chatId).collection("messages").document().set(message)

        messageTextField.setText("")


    }
}