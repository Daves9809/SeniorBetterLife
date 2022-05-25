package com.example.seniorbetterlife.maps

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.seniorbetterlife.R
import com.example.seniorbetterlife.maps.model.Place
import com.example.seniorbetterlife.maps.model.UserMap
import com.example.seniorbetterlife.util.MyImageRequestListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception
import java.util.*


private const val TAG = "DisplayMapsFragment"

class DisplayMapsFragment : Fragment(), MyImageRequestListener.Callback {

    private lateinit var userMap: UserMap

    private val viewModel: MapsViewModel by viewModels()
    private val listOfPlaces = mutableListOf<Place>()


    private val callback = OnMapReadyCallback { googleMap ->

        val boundsBuilder = LatLngBounds.Builder()
        val markerSet = Hashtable<String,Boolean>()

        for (place in userMap.places) {
            val latLng = LatLng(place.latitude, place.longitude)
            boundsBuilder.include(latLng)
            val marker = googleMap.addMarker(
                MarkerOptions().position(latLng).title(place.title)
                    .snippet(place.description)
            )

            if (marker != null) {
                markerSet.put(marker.id,false)
            }
        }

        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(),1000,1000,0))
        googleMap.setInfoWindowAdapter(CustomInfoWindowAdapter(this.requireActivity(),markerSet))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //pobranie argumentów z bundle, rodzaj userMapy jaki zostal wybrany
        userMap = arguments?.get("userMap") as UserMap
        //ustawienie tytułu action bar
        findNavController()

        return inflater.inflate(R.layout.fragment_display_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPointsOnMap(userMap.title)
        observeViewModel()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun observeViewModel() {
        viewModel.isUserDataAvailable.observe(viewLifecycleOwner) { userMap ->
        Log.d("Usermapa:", userMap!!.title)
        //dodanie miejsc na mapie do listy
        userMap.places.forEach { listOfPlaces.add(it) }
    }
    }


    inner class CustomInfoWindowAdapter(con:Context, val markerSet: Hashtable<String, Boolean>) : GoogleMap.InfoWindowAdapter {
        val context: Context = con

        // These are both view groups containing an ImageView with id "badge" and two
        // TextViews with id "title" and "snippet".
        private val window: View = layoutInflater.inflate(R.layout.custom_info_window, null)
        private val contents: View = layoutInflater.inflate(R.layout.custom_info_contents, null)

        override fun getInfoContents(marker: Marker): View? {
            render(marker, contents)
            return contents
        }

        override fun getInfoWindow(marker: Marker): View? {
            render(marker, window)
            return window
        }

        private fun render(marker: Marker, view: View) {
            //testing with sample image
            val imageView = view.findViewById<ImageView>(R.id.badge)

            //setting link from firebase to load in picasso
            val link = listOfPlaces.filter { it.description == marker.snippet }[0].urlOfGeopoint
            Log.d("listofplaces",link)

            //Setting image with Picasso
            var isImageLoaded: Boolean = markerSet.get(marker.id) == true
            if(isImageLoaded){
                Picasso.get().load(link).into(imageView);
            } else {
                isImageLoaded = true
                markerSet.put(marker.id, isImageLoaded)
                Picasso.get().load(link).into(imageView,InfoWindowRefresher(marker));
            }


            // Set the title and snippet for the custom info window
            val title: String? = marker.title
            val titleUi = view.findViewById<TextView>(R.id.title)

            titleUi.text = title

            val snippet: String? = marker.snippet
            val snippetUi = view.findViewById<TextView>(R.id.snippet)

            snippetUi.text = snippet

        }

    }

    override fun onFailure(message: String?) {
        Toast.makeText(this.context, "Fail to load: $message", Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess(dataSource: String) {
        Toast.makeText(this.context, "Loaded from: $dataSource", Toast.LENGTH_SHORT).show()
    }

}

internal class InfoWindowRefresher(private val markerToRefresh: Marker) :
    Callback {
    override fun onSuccess() {
        markerToRefresh.showInfoWindow()
    }

    override fun onError(e: Exception?) {
    }

}
