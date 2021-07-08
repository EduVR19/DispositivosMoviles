package com.example.dispositivosmoviles.activities

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dispositivosmoviles.adapters.MessageAdapter
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.SignUpActivity
import com.example.dispositivosmoviles.UsuarioClass
import com.example.dispositivosmoviles.models.Message
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_auth.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_chat.*
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.URL

class   ChatActivity : AppCompatActivity() {
    private var chatId = ""
    private var user = ""
    private val fileResult = 1
    private var db = Firebase.firestore
    private var path : String =""
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

        // Inicio Videollamada
        try {
            var options = JitsiMeetConferenceOptions.Builder()
                .setServerURL(URL(""))
                .setWelcomePageEnabled(false)
                .build()
        } catch (e: Exception) {
            e.printStackTrace()
            //Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
        }

        startVideoCallButton.setOnClickListener { startVideoCall() }
        // Fin Videollamada

    }
    private fun galeriaButton(){
        galeriaButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
            }
            intent.type ="*/*"
            startActivityForResult(intent,fileResult)
        }
    }

    // Inicio Videollamada
    private fun startVideoCall(){
        val roomName = "Break4PetsVideollamada"

        if(roomName.length > 0)
        {
            val options = JitsiMeetConferenceOptions.Builder()
                .setRoom(roomName)
                .build()

            JitsiMeetActivity.launch(this,options)
        }

    }
    // Fin Videollamada




    private fun initViews(){
        messagesRecylerView.layoutManager = LinearLayoutManager(this)
        messagesRecylerView.adapter = MessageAdapter(user)

        sendMessageButton.setOnClickListener { sendMessage(0) }

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


        var imagenes : Int
        docref.get()
            .addOnSuccessListener {
                 var user2 = it.toObject<UsuarioClass>()!!

                imagenes = user2.imagenes

                val ref = FirebaseStorage.getInstance().reference
                val folder : StorageReference = FirebaseStorage.getInstance().reference.child("${user}/${imagenes}.jpg")
                folder.putFile(mUri)
                    .addOnSuccessListener {
                        path=""
                        //imagenes++
                        //docref.update("imagenes",imagenes)
                        showAlert2("Sí se subio, checa firebase, los valores son ${user} y ${imagenes}")

                        ref.child("${user}/${imagenes}.jpg").downloadUrl
                            .addOnSuccessListener {
                                path= it.toString()

                                sendMessage(imagenes)
                                showAlert2("Sí consigiguio el downloarURl y todo")
                                imagenes++
                                docref.update("imagenes",imagenes)
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




    }

    private fun sendMessage(img : Int){

        if(messageTextField.text.toString() != ""){
            if(path == ""){
                val message = Message(
                    message = messageTextField.text.toString(),
                    from = user,
                    imgUrl = "",
                    numImg = 0
                )
                db.collection("chats").document(chatId).collection("messages").document().set(message)

            } else{
                val message = Message(
                    message = messageTextField.text.toString(),
                    from = user,
                    imgUrl = path,
                    numImg = img
                )


                db.collection("chats").document(chatId).collection("messages").document().set(message)



            }

        } else{
            val message = Message(
                message = "",
                from = user,
                imgUrl = path,
                numImg = img
            )


            db.collection("chats").document(chatId).collection("messages").document().set(message)
        }
        messageTextField.setText("")

    }
}