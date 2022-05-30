package com.example.seniorbetterlife.maps

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
import com.example.seniorbetterlife.data.repositories.FirebaseRepository
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
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)

        viewModel.getUserMaps()

        binding.rvMaps.layoutManager = LinearLayoutManager(this.context)
        //firebaseRepository.sendUserMap(generateSampleData())

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

    /*private fun generateSampleData(): List<UserMap> {
        return listOf(
            UserMap("Sport",
                listOf(
                    Place("Silownia plenerowa",
                        "Połącz aktywność fizyczną z radością przebywania na świeżym powietrzu. \n" +
                                "Rodzaje sprzętów: Biegacz, Prasa nożna, Wahadło, Orbitrek, Drabinka, Prasa ręczna, Tai Chi skośne oraz proste, Wioślarz, Rowerek",
                        50.04912800297385, 21.930073924827955,
                        "https://firebasestorage.googleapis.com/v0/b/seniorbetterlife.appspot.com/o/sport%2Fsilownia_plenerowa_przybyszowka.jpg?alt=media&token=b728e956-370a-4190-8e9e-dd49423e934b"),
                    Place("Siłownia plenerowa",
                        "Połącz aktywność fizyczną z radością przebywania na świeżym powietrzu.\n" +
                                "Rodzaje sprzętów: Biegacz, Prasa nożna, Wahadło, Orbitrek, Drabinka, Prasa ręczna, Tai Chi skośne oraz proste, Wioślarz, Rowerek",
                        50.04048383610212, 22.046102715437943,
                        "https://firebasestorage.googleapis.com/v0/b/seniorbetterlife.appspot.com/o/sport%2Fsilownia_plenerowa_mazowiecka.jpg?alt=media&token=5cb735f9-c1cb-4992-a2c9-7ccf911a8382"),
                    Place("Siłownia plenerowa",
                        "Połącz aktywność fizyczną z radością przebywania na świeżym powietrzu.\n" +
                                "Rodzaje sprzętów: Biegacz, Prasa nożna, Wahadło, Orbitrek, Drabinka, Prasa ręczna, Tai Chi skośne oraz proste, Wioślarz, Rowerek",
                        49.9693912923483, 21.987684698262427,
                        "https://firebasestorage.googleapis.com/v0/b/seniorbetterlife.appspot.com/o/sport%2Fsilownia_plenerowa_budziwoj.jpg?alt=media&token=97f6512c-e533-4d7c-8a32-8badb015f3de"),
                    Place("Siłownia w pomieszczeniu zamkniętym",
                        "Siłownia znajduje się w filii RDK: Biała \n" +
                                "Godziny otwarcia:\n" +
                                "poniedziałki, środy, piątki: 16:00 – 21:00\n" +
                                "wtorki, czwartki: 16:00 – 20:00",
                        49.995815397778905, 22.00427039641695,
                        "https://firebasestorage.googleapis.com/v0/b/seniorbetterlife.appspot.com/o/sport%2Fsilownia_zamknieta_filia_biala.png?alt=media&token=ba38f9f7-aad3-4c0d-8626-5c7231bdbdc8"),
                    Place("Siłownia w pomieszczeniu zamkniętym",
                        "Siłownia znajduje się w filii RDK: Przybyszówka \n" +
                                "Godziny otwarcia:\n" +
                                "od poniedziałku do piątku: 16:00 – 20:00",
                        50.05358233048447, 21.946241213605454,
                        "https://firebasestorage.googleapis.com/v0/b/seniorbetterlife.appspot.com/o/sport%2Fsilownia_zamknieta_filia_przybyszowka.png?alt=media&token=3835eb8e-a33a-4755-8697-01fc99f28330")
                )
            )

        )
    }*/

}