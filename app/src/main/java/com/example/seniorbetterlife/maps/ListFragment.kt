package com.example.seniorbetterlife.maps

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorbetterlife.data.repositories.FirebaseRepository
import com.example.seniorbetterlife.databinding.FragmentListBinding
import com.example.seniorbetterlife.databinding.FragmentRegisterBinding
import com.example.seniorbetterlife.fragments.login.LoginFragmentDirections
import com.example.seniorbetterlife.maps.model.Place
import com.example.seniorbetterlife.maps.model.UserMap

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val firebaseRepository = FirebaseRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)

        val userMaps = generateSampleData()

        binding.rvMaps.layoutManager = LinearLayoutManager(this.context)
        binding.rvMaps.adapter = MapsAdapter(this.context, userMaps, object: MapsAdapter.OnClickListener{
            override fun onItemClick(position: Int) {
                val myArgs = ListFragmentDirections.actionListFragmentToDisplayMapsFragment(position.toString()).actionId
                val bundle = bundleOf("userMap" to userMaps[position])
                findNavController().navigate(myArgs,bundle)
                Log.d("ListFragment","item on position:$position clicked, title = ${userMaps[position].title}")
            }
        })

        //firebaseRepository.sendUserMap(userMaps)

        return binding.root

    }
    private fun generateSampleData(): List<UserMap> {
        return listOf(
            UserMap("Uniwersytety III wieku",
                listOf(
                    Place("Uniwersytet Rzeszowski",
                        "Słuchaczem UTW może zostać osoba 55+ nieaktywna zawodowo (emeryt, rencista - konieczność okazania aktualnej legitymacji ZUS)," +
                                " która złoży deklarację członkowską wraz ze zdjęciem, opłaci wpisowe i składki na dany rok akademicki " +
                                "oraz będzie aktywnie uczestniczyć w zajęciach.",
                        50.030315927850026, 22.015712896417885,""),
                    Place("Akademia 50+ przy WSIiZ",
                        "Oferta Akademii 50+ skierowana jest do każdego, kto ma ukończone lat 50 i pragnie pogłębiać swoją wiedzę \n" +
                                "Godziny otwarcia: - \n" +
                                "Tel: 17 866 12 78",
                        50.04901660881597, 21.98172305593496,""),
                    Place("Diecezja Rzeszowska",
                    "Słuchaczami UTWDRz mogą zostać osoby: po ukończeniu 50. roku życia, po ukończeniu aktywności zawodowej.",
                        50.03007274438139, 22.04380254243994,"")
                )
            )

        )
    }

}