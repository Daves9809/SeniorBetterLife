package com.example.seniorbetterlife.ui.senior.helpPart.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorbetterlife.R
import com.example.seniorbetterlife.ui.senior.helpPart.model.UserTask

class CompletedTasksAdapter(private val listOfCompletedUserTasks: List<UserTask>) :
    RecyclerView.Adapter<CompletedTasksAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CompletedTasksAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design_completed_tasks,parent,false)
        return CompletedTasksAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompletedTasksAdapter.ViewHolder, position: Int) {
        val userTask = listOfCompletedUserTasks[position]

        holder.tvDescription.text = userTask.description
        holder.tvNumberOfTask.text = "Zadanie z dnia ${userTask.date}"
    }

    override fun getItemCount(): Int {
        return listOfCompletedUserTasks.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNumberOfTask: TextView = itemView.findViewById(R.id.tvTaskCompleted)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescriptionCompleted)
    }
}