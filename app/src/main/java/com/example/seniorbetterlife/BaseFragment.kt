package com.example.seniorbetterlife

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.seniorbetterlife.activities.MainActivity

abstract class BaseFragment : Fragment() {
   protected fun startApp(){
       val intent = Intent(requireContext(), MainActivity::class.java).apply {
           flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) // flag activity zabezpieczaja przed powrotem
       }
       startActivity(intent)
   }
}