package com.example.seniorbetterlife.ui.senior.pillReminder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorbetterlife.R
import com.example.seniorbetterlife.data.model.Medicament

class AddDrugAdapter(): RecyclerView.Adapter<AddDrugAdapter.ViewHolder>()  {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{
        fun onDeleteClickListener(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    private val myDataSet = mutableListOf<Medicament>()

    inner class ViewHolder(view: View, listener: onItemClickListener): RecyclerView.ViewHolder(view) {
        val tvDrugName: TextView
        val tvHoursOfTake: TextView

        init {
            tvDrugName = view.findViewById(R.id.tvDrugNamee)
            tvHoursOfTake = view.findViewById(R.id.tvFrequencyDose)

            //set listener functions to relevant views
            itemView.findViewById<TextView>(R.id.tvDelete).setOnClickListener {
                listener.onDeleteClickListener(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_view_design_pill_schedule, viewGroup, false)

        return ViewHolder(view,mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvDrugName.text = myDataSet[position].medicine
        val listOfHours: MutableList<String> = mutableListOf()
        for (dailyDose in myDataSet[position].dailyDoses) {
            listOfHours.add(dailyDose.hourOfTake)
        }
        val hoursOfTake = hoursOfTake(listOfHours)
        holder.tvHoursOfTake.text = hoursOfTake
    }

    private fun hoursOfTake(list: List<String>): String {
        return when(list.size){
            1 -> "1 raz dziennie ${list[0]}"
            2 -> "2 razy dziennie ${list[0]} i ${list[1]}"
            3 -> "3 razy dziennie ${list[0]} i ${list[1]} i ${list[2]}"
            else -> "error"
        }
    }

    override fun getItemCount(): Int {
        return myDataSet.size
    }

    fun setData(newData: List<Medicament>) {
        myDataSet.clear()
        myDataSet.addAll(newData)
        notifyDataSetChanged()
    }
}