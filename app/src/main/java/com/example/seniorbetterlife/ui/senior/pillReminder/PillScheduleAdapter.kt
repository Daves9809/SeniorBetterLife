package com.example.seniorbetterlife.ui.senior.pillReminder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorbetterlife.R
import com.example.seniorbetterlife.ui.senior.pillReminder.model.Dose

class PillScheduleAdapter(
    private val frequency: Int, private val dose: Dose) :
    RecyclerView.Adapter<PillScheduleAdapter.ViewHolder>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{
        fun onChooseHourClick(position: Int, view: TextView)
        fun sendValues(position: Int, selectedSpinnerValue: String, selectedHour: String)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }


    class ViewHolder(view: View, listener: onItemClickListener) : RecyclerView.ViewHolder(view) {
        val spinnerDoseCount: Spinner
        val tvNumberOfDose: TextView
        val tvHour: TextView
        var isHourChoosen: Boolean = false
        var isNumberOfDoseChoosen: Boolean = false
        val listenerr = listener

        init {
            spinnerDoseCount = view.findViewById(R.id.spinnerDoseCount)
            tvNumberOfDose = view.findViewById(R.id.tvNumberOfDose)
            tvHour = view.findViewById<EditText>(R.id.tvSetHour)
            itemView.findViewById<TextView>(R.id.tvSetHour).setOnClickListener {
                listener.onChooseHourClick(adapterPosition,tvHour)
                isHourChoosen = true
            }
            isNumberOfDoseChoosen = itemView.findViewById<Spinner>(R.id.spinnerDoseCount).callOnClick()
        }



    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PillScheduleAdapter.ViewHolder {
        //
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recyclerview_pill_schedule, parent, false)
        return ViewHolder(view,mListener)
    }

    override fun onBindViewHolder(holder: PillScheduleAdapter.ViewHolder, position: Int) {
        holder.tvNumberOfDose.text = "${convertPosition(position)}  dawka"
        // spinner dose count
        //setting kind of adapter for spinner
        val list = when (dose) {
            Dose.TABLETS ->{
                PillHelper.listOfTablets
            }
            Dose.MILIGRAMS -> {
                PillHelper.listOfMiligrams
            }
            Dose.DROPS ->{
                PillHelper.listOfDrops
            }
        }
        val dataAdapter = ArrayAdapter<Any>(
            holder.spinnerDoseCount.context,
            android.R.layout.simple_spinner_item,
            list)

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.spinnerDoseCount.adapter = dataAdapter
        holder.spinnerDoseCount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(holder.isHourChoosen)
                    holder.listenerr.sendValues(holder.adapterPosition,list[p2].toString(),holder.tvHour.text.toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Nothing selected
            }

        }

    }

    override fun getItemCount(): Int {
        return frequency
    }

    private fun convertPosition(position: Int): String {
        return when (position) {
            0 -> "Pierwsza"
            1 -> "Druga"
            2 -> "Trzecia"
            else -> "Error"
        }
    }

}