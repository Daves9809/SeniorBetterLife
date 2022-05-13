package com.example.seniorbetterlife.maps

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorbetterlife.R
import com.example.seniorbetterlife.maps.model.UserMap

class MapsAdapter(val context: Context?, val userMaps: List<UserMap>, val onClickListener: OnClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_recuclerview_maps, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val userMap = userMaps[position]
        holder.itemView.setOnClickListener{
            onClickListener.onItemClick(position)
        }
        val textView = holder.itemView.findViewById<TextView>(R.id.tvItem)
        textView.text = userMap.title
    }

    override fun getItemCount(): Int {
        return userMaps.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

}
