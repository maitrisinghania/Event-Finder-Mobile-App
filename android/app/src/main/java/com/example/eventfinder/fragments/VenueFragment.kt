package com.example.eventfinder.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import at.blogc.android.views.ExpandableTextView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.eventfinder.R
import com.example.eventfinder.databinding.FragmentVenueBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONObject


class VenueFragment(private val data:JSONObject) : Fragment() {
    private var _binding: FragmentVenueBinding? = null
    private val binding get() = _binding!!


    var lng:Double = 0.0
    var lat:Double = 0.0





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {




        _binding = FragmentVenueBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        var venue = data.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name")

        var editName = view.findViewById<TextView>(R.id.edit_name)
        editName.text = venue
        var arr = ArrayList<String>()



        var textAddress = view.findViewById<TextView>(R.id.text_address)
        var textCity = view.findViewById<TextView>(R.id.text_city)
        var textContact = view.findViewById<TextView>(R.id.text_contact)
        var textHour = view.findViewById<TextView>(R.id.text_hour)
        var textChild = view.findViewById<TextView>(R.id.text_child)
        var textGeneral = view.findViewById<TextView>(R.id.text_general)

        var editAddress = view.findViewById<TextView>(R.id.edit_address)
        var editCity = view.findViewById<TextView>(R.id.edit_city)
        var editContact = view.findViewById<TextView>(R.id.edit_contact)
        var editHour = view.findViewById<ExpandableTextView>(R.id.edit_hour)
        var editChild = view.findViewById<ExpandableTextView>(R.id.edit_child)
        var editGeneral = view.findViewById<ExpandableTextView>(R.id.edit_general)

        var venueHours = view.findViewById<CardView>(R.id.venue_hours)

        editChild.setInterpolator(OvershootInterpolator())
        editHour.setInterpolator(OvershootInterpolator())
        editGeneral.setInterpolator(OvershootInterpolator())

        editName.movementMethod = ScrollingMovementMethod()
        editName.isSelected = true

        editAddress.movementMethod = ScrollingMovementMethod()
        editAddress.isSelected = true

        editCity.movementMethod = ScrollingMovementMethod()
        editCity.isSelected = true

        editContact.movementMethod = ScrollingMovementMethod()
        editContact.isSelected = true



        var cityArr = ArrayList<String>()
        val queue = Volley.newRequestQueue(context)
        val url = "https://reactticketmaster.wl.r.appspot.com/venue_details?keyword=${venue}"


        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d("data for venue response:", "" + response)


                val venuesData = response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0)
                Log.d("venue data:",""+venuesData)
                if(venuesData.has("address")){
                    Log.d("entered address:",""+venuesData.getJSONObject("address"))
                    if(venuesData.getJSONObject("address").has("line1")){



                        editAddress.text = venuesData.getJSONObject("address").getString("line1")

                    }else{

                        editAddress.visibility = View.GONE
                        textAddress.visibility = View.GONE

                    }

                }
                else{
                    editAddress.visibility = View.GONE
                    textAddress.visibility = View.GONE
                }

                if(venuesData.has("city")){
                    cityArr.add(venuesData.getJSONObject("city").getString("name"))
                }

                if(venuesData.has("state")){
                    cityArr.add(venuesData.getJSONObject("state").getString("name"))
                }
                if(cityArr!== null && cityArr.size == 2){
                    editCity.text = cityArr.joinToString(", ")
                }else if(cityArr!==null && cityArr.size == 1){
                    editCity.text = cityArr[0]
                }
                else{
                    editCity.visibility = View.GONE
                    textCity.visibility = View.GONE
                }

                if(venuesData.has("boxOfficeInfo")){
                    if(venuesData.getJSONObject("boxOfficeInfo").has("phoneNumberDetail")){
                        editContact.text = venuesData.getJSONObject("boxOfficeInfo").getString("phoneNumberDetail")
                    }
                    else{
                        editContact.visibility = View.GONE
                        textContact.visibility = View.GONE
                    }

                    if(venuesData.getJSONObject("boxOfficeInfo").has("openHoursDetail")){
                        editHour.text = venuesData.getJSONObject("boxOfficeInfo").getString("openHoursDetail")
                    }
                    else{
                        editHour.visibility = View.GONE
                        textHour.visibility = View.GONE
                        arr.add("false")
                    }

                }
                else{
                    editContact.visibility = View.GONE
                    textContact.visibility = View.GONE
                    editHour.visibility = View.GONE
                    textHour.visibility = View.GONE
                    arr.add("false")
                }


                if(venuesData.has("generalInfo")){
                    if(venuesData.getJSONObject("generalInfo").has("generalRule")){
                        val general = venuesData.getJSONObject("generalInfo").getString("generalRule")
                        editGeneral.text = general

                    }
                    else{
                        editGeneral.visibility = View.GONE
                        textGeneral.visibility = View.GONE
                    }

                    if(venuesData.getJSONObject("generalInfo").has("childRule")){
                        editChild.text = venuesData.getJSONObject("generalInfo").getString("childRule")
                    }
                    else{
                        editChild.visibility = View.GONE
                        textChild.visibility = View.GONE
                    }

                }
                else{
                    editGeneral.visibility = View.GONE
                    textGeneral.visibility = View.GONE
                    editChild.visibility = View.GONE
                    textChild.visibility = View.GONE
                    arr.add("false")

                }

                if(venuesData.has("location")){

                    lng = venuesData.getJSONObject("location").getString("longitude").toDouble()
                    lat = venuesData.getJSONObject("location").getString("latitude").toDouble()

                }


                val mapView = view.findViewById<MapView>(R.id.map_view)
                mapView.onCreate(savedInstanceState)
                mapView.getMapAsync { googleMap ->
                    // Configure the map here
                    val marker = googleMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(lat, lng))
                            .title(venue)
                    )
                    marker.showInfoWindow()
                    val cameraPosition = CameraPosition.Builder()
                        .target(marker.position)
                        .zoom(16f) // set the zoom level
                        .build()
                    val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
                    googleMap.moveCamera(cameraUpdate)

                }

                if(arr.size ===2){

                    venueHours.visibility = View.GONE

                }




















            },
            {
                Log.d("data not received","for events")

            }
        )
        queue.add(jsonObjectRequest)

        editChild.setOnClickListener {
            if (editChild.isExpanded) {
                editChild.collapse()

            } else {
                editChild.expand()

            }
        }

        editHour.setOnClickListener {
            if (editHour.isExpanded) {
                editHour.collapse()

            } else {
                editHour.expand()

            }
        }

        editGeneral.setOnClickListener {
            if (editGeneral.isExpanded) {
                editGeneral.collapse()

            } else {
                editGeneral.expand()

            }
        }





    }






}