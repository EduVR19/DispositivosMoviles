package com.example.dispositivosmoviles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList

class MyAdapter(private val userList: ArrayList<User>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_reserve, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {
        val user: User = userList[position]
        holder.correo.text = user.correo
        holder.fechafin.text = user.fechafin
        holder.fechainicio.text = user.fechainicio
        holder.nota.text = user.nota
        holder.codigo.text = user.codigo
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var correo: TextView = itemView.findViewById(R.id.correoNameText)
        var fechafin: TextView = itemView.findViewById(R.id.finNameText)
        var fechainicio: TextView = itemView.findViewById(R.id.inicioNameText)
        var nota: TextView = itemView.findViewById(R.id.notasNameText)
        var codigo: TextView = itemView.findViewById(R.id.codigosNameText)

    }


}