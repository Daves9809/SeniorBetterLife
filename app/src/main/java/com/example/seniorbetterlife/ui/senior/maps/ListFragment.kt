package com.example.seniorbetterlife.ui.senior.maps

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorbetterlife.databinding.FragmentListBinding

class ListFragment : Fragment() {

    private val LIST_FRAGMENT_DEBUG = "ListFragment"

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MapsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)

        viewModel.getUserMaps()

        binding.rvMaps.layoutManager = LinearLayoutManager(this.context)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observingData()

    }

    private fun observingData() {
        viewModel.isUserMapsAvailable.observe(viewLifecycleOwner,
        Observer { listofUserMaps ->
            Log.d(LIST_FRAGMENT_DEBUG,listofUserMaps.toString())
            binding.rvMaps.adapter = MapsAdapter(this.context, listofUserMaps!!, object: MapsAdapter.OnClickListener{
                override fun onItemClick(position: Int) {
                    val myArgs = ListFragmentDirections.actionListFragmentToDisplayMapsFragment(position.toString()).actionId
                    val bundle = bundleOf("userMap" to listofUserMaps[position])
                    findNavController().navigate(myArgs,bundle)
                    Log.d("ListFragment","item on position:$position clicked, title = ${listofUserMaps[position].title}")
                }
            })
        })
    }

}