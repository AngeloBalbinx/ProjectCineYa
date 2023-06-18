package com.example.projectcineya

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class PeliculaVH (itemView: View) : RecyclerView.ViewHolder(itemView){
    var recImagen: ImageView
    var recTitulo: TextView
    var recDirector: TextView
    var recGenero: TextView
    var recCine: TextView
    var recCard: CardView
    init{
        recImagen = itemView.findViewById(R.id.recImagen)
        recCard = itemView.findViewById(R.id.recCard)
        recDirector=itemView.findViewById(R.id.recDirector)
        recGenero=itemView.findViewById(R.id.recGenero)
        recCine=itemView.findViewById(R.id.recCine)
        recTitulo=itemView.findViewById(R.id.recTitulo)
    }
}