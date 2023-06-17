package com.example.projectcineya

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.content.Context
class PeliculaAdapter (private val context: Context, private var dataList: List<PeliculaClass>) : RecyclerView.Adapter<PeliculaVH>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeliculaVH {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.recylcer_item,parent,false)
        return PeliculaVH(view)

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: PeliculaVH, position: Int) {
        Glide.with(context).load(dataList[position].imagen).into(holder.recImagen)
        holder.recTitulo.text=dataList[position].titulo
        holder.recDirector.text=dataList[position].director
        holder.recGenero.text=dataList[position].genero

    }



}