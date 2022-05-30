package com.example.seniorbetterlife.helpPart

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.seniorbetterlife.R
import java.lang.ClassCastException

class AddTaskDialogFragment: DialogFragment() {

    private lateinit var  listener: DialogListener

    interface  DialogListener {
        fun onDialogPositiveClick(kindOfHelp: String)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate(R.layout.fragment_add_task_dialog, container, false)

        val etProblem = rootView.findViewById<EditText>(R.id.etDescribeYourProblem)

        rootView.findViewById<Button>(R.id.btnSubmit).setOnClickListener {
            listener.onDialogPositiveClick(etProblem.text.toString())
            dismiss()
        }
        rootView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            listener.onDialogNegativeClick(this)
            dismiss()
        }


        return rootView
    }

    //check whether the interface was actually implemented by the parent activity/fragment
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            listener = context as DialogListener
        }catch(e: ClassCastException) {
            throw ClassCastException((context.toString() +
                    "must implement DialogFragment"))
        }
    }
}