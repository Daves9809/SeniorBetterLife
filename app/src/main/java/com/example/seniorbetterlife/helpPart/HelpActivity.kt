package com.example.seniorbetterlife.helpPart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.seniorbetterlife.databinding.ActivityHelpBinding
import com.example.seniorbetterlife.helpPart.adapters.ViewPagerAdapter


class HelpActivity : AppCompatActivity(), AddTaskDialogFragment.DialogListener{

    private lateinit var binding: ActivityHelpBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpTabs()

    }

    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(ListOfActiveTasksFragment(),"Aktywne")
        adapter.addFragment(ListOfCompletedTasksFragment(),"Ukończone")
        binding.viewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewPager)

    }

    override fun onDialogPositiveClick(kindOfHelp: String, phoneNumber: String) {
        Toast.makeText(this,"$kindOfHelp, $phoneNumber",Toast.LENGTH_SHORT).show()

    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        Toast.makeText(this,"NEGATIVE",Toast.LENGTH_SHORT).show()
    }

}