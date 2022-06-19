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
import com.example.seniorbetterlife.databinding.FragmentListOfActiveTasksBinding
import com.example.seniorbetterlife.ui.senior.helpPart.adapters.ActiveTaskAdapter
import com.example.seniorbetterlife.ui.senior.helpPart.model.UserTask


class ListOfActiveTasksFragment : Fragment() {

    private var _binding: FragmentListOfActiveTasksBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SharedViewModel by activityViewModels()

    private val TAG = this.tag
    private lateinit var recyclerView: RecyclerView
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListOfActiveTasksBinding.inflate(inflater,container,false)

        onClickListeners()

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        refreshApp()

        return binding.root
    }

    private fun refreshApp() {
        binding.swipeToRefresh.setOnRefreshListener {
            Toast.makeText(requireContext(),"Dane odświeżone!",Toast.LENGTH_SHORT).show()
            binding.swipeToRefresh.isRefreshing = false
            viewModel.getUserTasks(user.email!!)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        viewModel.getUserData()
    }

    private fun observeData() {
        //download user
        viewModel.isUserDataAvailable.observe(viewLifecycleOwner, Observer {
            //Declare tasks
            user = it!!
            viewModel.getUserTasks(it.email!!)

        })

        viewModel.isUserTaskRetrieved.observe(viewLifecycleOwner, Observer { listOfUserTasks ->
            val listOfUserTaskss = listOfUserTasks as List<UserTask>

            val listOfActiveTasks = listOfUserTaskss.filter { !it.finished }

            val adapter = ActiveTaskAdapter(listOfActiveTasks)
            recyclerView.adapter = adapter

            //buttons listeners
            adapter.setOnItemClickListener(object : ActiveTaskAdapter.onItemClickListener{

                override fun onPositiveButtonClick(position: Int) {
                    viewModel.setUserTaskToCompleted(listOfActiveTasks[position])
                    Toast.makeText(requireContext(),"Zadanie zostało ukończone",Toast.LENGTH_SHORT).show()
                }

                override fun onNegativeButtonClick(position: Int) {
                    viewModel.deleteUserTask(listOfActiveTasks[position])
                    Toast.makeText(requireContext(),"Zadanie zostało usunięte",Toast.LENGTH_SHORT).show()
                }

            })

        })
    }

    private fun onClickListeners() {
        val fab = binding.fabAddTask
        fab.setOnClickListener {
            val dialog = AddTaskDialogFragment()
            dialog.show(parentFragmentManager,"addTaskDialog")
        }
    }



}