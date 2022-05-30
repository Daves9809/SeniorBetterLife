package com.example.seniorbetterlife.helpPart.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorbetterlife.R
import com.example.seniorbetterlife.helpPart.model.UserTask

class ActiveTaskAdapter(private val listOfUserTasks: List<UserTask>): RecyclerView.Adapter<ActiveTaskAdapter.ViewHolder>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{
        fun onPositiveButtonClick(position: Int)
        fun onNegativeButtonClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveTaskAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design,parent,false)
        return ViewHolder(view,mListener)
    }

    //binds the list items to a view
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ActiveTaskAdapter.ViewHolder, position: Int) {

        val userTask = listOfUserTasks[position]

        holder.tvNumberOfTask.text = "Zadanie z dnia ${userTask.date}"
        holder.tvDescription.text = userTask.description
    }

    //return number of items in the list
    override fun getItemCount(): Int {
        return listOfUserTasks.size
    }

    // Holds views for adding them to texts
    class ViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val tvNumberOfTask: TextView = itemView.findViewById(R.id.tvTask)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val btnFinishTask: Button = itemView.findViewById(R.id.btnTaskCompleted)
        val btnResign: Button = itemView.findViewById(R.id.btnResign)

        //set listener functions to relevant views
        init {
            itemView.findViewById<Button>(R.id.btnResign).setOnClickListener {
                listener.onNegativeButtonClick(adapterPosition)
            }
            itemView.findViewById<Button>(R.id.btnTaskCompleted).setOnClickListener {
                listener.onPositiveButtonClick(adapterPosition)
            }
        }

    }
}