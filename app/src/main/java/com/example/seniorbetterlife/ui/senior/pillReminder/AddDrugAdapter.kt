package com.example.seniorbetterlife.ui.senior.pillReminder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorbetterlife.R
import com.example.seniorbetterlife.data.model.Medicament

class AddDrugAdapter(private val dataSet: List<Medicament>): RecyclerView.Adapter<AddDrugAdapter.ViewHolder>()  {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvDrugName: TextView
        val tvHoursOfTake: TextView

        init {
            tvDrugName = view.findViewById(R.id.tvDrugNamee)
            tvHoursOfTake = view.findViewById(R.id.tvFrequencyDose)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_view_design_pill_schedule, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvDrugName.text = dataSet[position].medicine
        val listOfHours: MutableList<String> = mutableListOf()
        for (dailyDose in dataSet[position].dailyDoses) {
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
        return dataSet.size
    }
}