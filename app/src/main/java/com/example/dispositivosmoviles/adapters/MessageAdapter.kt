package com.example.dispositivosmoviles.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.UsuarioClass
import com.example.dispositivosmoviles.models.Message
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main_client.*
import kotlinx.android.synthetic.main.item_image_message.view.*
import kotlinx.android.synthetic.main.item_message.view.*
import kotlinx.android.synthetic.main.item_message.view.myMessageLayout
import kotlinx.android.synthetic.main.item_message.view.myMessageTextView
import kotlinx.android.synthetic.main.item_message.view.otherMessageLayout
import kotlinx.android.synthetic.main.item_message.view.otherMessageName
import kotlinx.android.synthetic.main.item_message.view.othersMessageTextView


class MessageAdapter(private val user: String): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private var messages: List<Message> = emptyList()
    private var db = Firebase.firestore

    fun setData(list: List<Message>){
        messages = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                //R.layout.item_message,
                R.layout.item_image_message,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        val storageRef = FirebaseStorage.getInstance().reference

        if(user == message.from){
            holder.itemView.myMessageLayout.visibility = View.VISIBLE
            holder.itemView.otherMessageLayout.visibility = View.GONE
            holder.itemView.myMessageTextView.text = message.message

            /*if(message.imgUrl != ""){
                var imagen = Uri.parse(message.imgUrl)


                holder.itemView.myImage.setImageURI(imagen)
                //holder.itemView.imageView4.setImageURI(imagen)
            }*/
            if(message.imgUrl != ""){
                var imageRef = storageRef.child("raul.rrbg@gmail.com/1.jpg")

                val ONE_MEGABYTE: Long = 1024 * 1024

                imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                    if(bitmap==null){

                    } else{
                        holder.itemView.myImage.setImageBitmap(bitmap)
                    }

                }.addOnFailureListener{

                }
            }

        } else {
            holder.itemView.myMessageLayout.visibility = View.GONE
            holder.itemView.otherMessageLayout.visibility = View.VISIBLE

            holder.itemView.otherMessageName.text = message.from
            holder.itemView.othersMessageTextView.text = message.message

            if(message.imgUrl != ""){
                var imageRef = storageRef.child("raul.rrbg@gmail.com/1.jpg")

                val ONE_MEGABYTE: Long = 1024 * 1024

                imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                    if(bitmap==null){

                    } else{
                        holder.itemView.otherImage.setImageBitmap(bitmap)
                    }

                }.addOnFailureListener{

                }
            }
        }

    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)


}