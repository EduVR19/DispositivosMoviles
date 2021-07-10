package com.example.dispositivosmoviles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList

class MyAdapter2(private val userList: ArrayList<Reserve>) : RecyclerView.Adapter<MyAdapter2.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter2.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_reserve_client, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyAdapter2.MyViewHolder, position: Int) {
        val user: Reserve = userList[position]
        holder.codigo.text = user.codigo
        holder.fechafin.text = user.fechafin
        holder.fechainicio.text = user.fechainicio
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var codigo: TextView = itemView.findViewById(R.id.codigoReserve)
        var fechafin: TextView = itemView.findViewById(R.id.fechaFinalReserve)
        var fechainicio: TextView = itemView.findViewById(R.id.fechaInicialReserve)
    }


}