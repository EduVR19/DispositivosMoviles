package com.example.dispositivosmoviles.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dispositivosmoviles.*
import com.example.dispositivosmoviles.models.Chat
import com.example.dispositivosmoviles.adapters.ChatAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_list_of_chats_admin.*
import kotlinx.android.synthetic.main.activity_list_of_chats_admin.listChatsRecyclerView
import kotlinx.android.synthetic.main.activity_reserve_admin.*
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.URL
import java.util.*

class ListOfChatsAdminActivity : AppCompatActivity() {
    private var user = ""

    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_chats_admin)

        intent.getStringExtra("user")?.let { user = it }

        if (user.isNotEmpty()){
            initViews()
        }
        backButton.setOnClickListener {
            startActivity(Intent(this, MainAdminActivity::class.java))
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

        startVideoCallButtonAdmin.setOnClickListener { startVideoCall() }
        // Fin Videollamada
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

        val docref = db.collection("Usuarios").document(user)
        docref.get().addOnSuccessListener {
            val user = it.toObject<UsuarioClass>()
            if (user?.correo == "canamar.samuel@gmail.com")
            {
                newChatButtonAdmins.setOnClickListener { newChat() }
            }
            else
            {
                newChatButtonAdmins.setOnClickListener { showAlertOtherAdmin() }
            }
        }
        listChatsRecyclerView.layoutManager = LinearLayoutManager(this)
        listChatsRecyclerView.adapter =
            ChatAdapter { chat ->
                chatSelected(chat)
            }

        val userRef = db.collection("Usuarios").document(user)

        userRef.collection("chats")
            .get()
            .addOnSuccessListener { chats ->
                val listChats = chats.toObjects(Chat::class.java)

                (listChatsRecyclerView.adapter as ChatAdapter).setData(listChats)
            }

        userRef.collection("chats")
            .addSnapshotListener { chats, error ->
                if(error == null){
                    chats?.let {
                        val listChats = it.toObjects(Chat::class.java)

                        (listChatsRecyclerView.adapter as ChatAdapter).setData(listChats)
                    }
                }
            }
    }

    private fun showAlertOtherAdmin() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Usted no tiene permitido crear chat grupal")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun chatSelected(chat: Chat){
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("chatId", chat.id)
        intent.putExtra("user", user)
        startActivity(intent)
    }

    private fun newChat(){
        val chatId = UUID.randomUUID().toString()
        val otherUser = "samuel.canamar@gmail.com"
        val otherotherUser = "axelillooo@gmail.com"
        val Usuarios = listOf(user, otherUser, otherotherUser)

        val chat = Chat(
            id = chatId,
            name = "Chat con $otherUser y $otherotherUser",
            Usuarios = Usuarios
        )

        db.collection("chats").document(chatId).set(chat)
        db.collection("Usuarios").document(user).collection("chats").document(chatId).set(chat)
        db.collection("Usuarios").document(otherUser).collection("chats").document(chatId).set(chat)
        db.collection("Usuarios").document(otherotherUser).collection("chats").document(chatId).set(chat)

        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("chatId", chatId)
        intent.putExtra("user", user)
        startActivity(intent)
    }
}