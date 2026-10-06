package com.nicolas.hor_scopo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HoroscopeAdapter(
    var items: List<Horoscope>,
    val onItemClick: (position: Int) -> Unit     // <-- Funcion lambda, bloques de código sin nombre que se pueden almacenar en variables, desconoce el contenido
) : RecyclerView.Adapter<HoroscopeViewHolder>() {

    //Cual es la vista de cada elemento
    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): HoroscopeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_horoscope, parent, false)
        return HoroscopeViewHolder(view)
    }

    //Cuales son los datos del elemento que esta en tal posicion
    override fun onBindViewHolder( holder: HoroscopeViewHolder, position: Int ) {
        val horoscope = items[position]
        holder.render(horoscope)
        holder.itemView.setOnClickListener {
            //Navegar al detalle del signo clickado
            onItemClick(position)
        }
    }

    //Cuantos elemetos tiene que mostrar
    override fun getItemCount(): Int {
        return items.size
    }

    fun updateData(dataSet: List<Horoscope>){
        items = dataSet
        notifyDataSetChanged()
    }
}

class HoroscopeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val signImageView: ImageView = view.findViewById(R.id.signImageView)
    val nameTextView: TextView = view.findViewById(R.id.nameTextView)
    val datesTextView: TextView = view.findViewById(R.id.datesTextView)

    fun render(horoscope: Horoscope) {
        nameTextView.setText(horoscope.name)
        datesTextView.setText(horoscope.dates)
        signImageView.setImageResource(horoscope.sign)
    }
}