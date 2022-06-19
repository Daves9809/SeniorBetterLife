package com.example.seniorbetterlife.ui.senior.helpPart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorbetterlife.data.model.User
import com.example.seniorbetterlife.databinding.FragmentListOfCompletedTasksBinding
import com.example.seniorbetterlife.ui.senior.helpPart.adapters.CompletedTasksAdapter
import com.example.seniorbetterlife.ui.senior.helpPart.model.UserTask


class ListOfCompletedTasksFragment : Fragment() {

    private var _binding: FragmentListOfCompletedTasksBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var user: User

    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListOfCompletedTasksBinding.inflate(inflater,container,false)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //swipe refresher to updateListOfTasks
        refreshApp()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        viewModel.getUserData()
    }

    private fun observeData() {
        viewModel.isUserDataAvailable.observe(viewLifecycleOwner, Observer {
            //Declare tasks
            user = it!!
            viewModel.getUserTasks(it.email!!)

        })

        viewModel.isUserTaskRetrieved.observe(viewLifecycleOwner, Observer { listOfUserTasks ->
            val listOfUserTaskss = listOfUserTasks as List<UserTask>
            //filter for completedTasks
            val listOfCompletedTasks = listOfUserTaskss.filter { it.finished }

            //bindowanie taskow do recyclerView
            val adapter = CompletedTasksAdapter(listOfCompletedTasks)
            recyclerView.adapter = adapter

        })
    }

    private fun refreshApp() {
        binding.swipeToRefresh.setOnRefreshListener {
            Toast.makeText(requireContext(),"Dane odświeżone!", Toast.LENGTH_SHORT).show()
            binding.swipeToRefresh.isRefreshing = false
            viewModel.getUserTasks(user.email!!)
        }
    }

}