package com.example.eventfinder

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox

import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class TableFragment : Fragment() {



    data class Event(var name: String, var date: String, var time: String, var venue: String, var genre: String, var img: String, var timeDate: LocalDateTime, var event_id: String)

    var name = ""
    var img = ""
    var dateEvent = ""
    var genre = ""
    var venue = ""
    var eventArr = ArrayList<Event>()
    var timeEvent = ""



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_table, container, false)



        val bundle = arguments
        var keyword = bundle?.getString("keyword")
        if(keyword === "All"){
            keyword = "Default"
        }
        val distance = bundle?.getInt("radius")
        val longitude = bundle?.getDouble("longitude")
        val latitude = bundle?.getDouble("latitude")
        val category = bundle?.getString("category")
        val progress = view.findViewById<ProgressBar>(R.id.progress)
        val noEvents = view.findViewById<TextView>(R.id.noEvent)
        val recyclerView: RecyclerView =view.findViewById(R.id.recycler)



        Log.d("bundle table:",""+bundle)

        if (distance != null) {
            if (latitude != null) {
                if (longitude != null) {
                    events(keyword.toString(),distance,latitude,longitude,category.toString(),progress,noEvents,recyclerView)
                }
            }
        }

        val backButton = view.findViewById(R.id.back_to_search) as Button
        backButton.setOnClickListener { Navigation.findNavController(view).navigate(R.id.navigateToFormFragment,bundle) }




        return view
    }




    private fun events(keyword :String,distance : Int,latitude : Double,longitude : Double,category : String, progress: ProgressBar,noEvents:TextView,recyclerView: RecyclerView){



        val eventObj = ArrayList<String>()
        val queue = Volley.newRequestQueue(context)
        val url = "https://reactticketmaster.wl.r.appspot.com/events?keyword=${keyword}&category=${category}&longitude=${longitude}&latitude=${latitude}&radius=${distance}"

        Log.d("url for location:", ""+url)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->

                Log.d("events response:", ""+response)
                Log.d("progress visibility:" ,""+progress.visibility)
                if(response.has("_embedded") &&  (response != null)){
                    if(response.getJSONObject("_embedded").has("events") ){

                        progress.visibility = View.GONE
                        noEvents.visibility = View.GONE

                        val events = response.getJSONObject("_embedded").getJSONArray("events")





                        for(i in 0 until  events.length()){

                            name = events.getJSONObject(i).getString("name")
                            var event_id = events.getJSONObject(i).getString("id")
                            dateEvent = events.getJSONObject(i).getJSONObject("dates").getJSONObject("start").getString("localDate")


                            if(events.getJSONObject(i).has("images")){

                                if(events.getJSONObject(i).getJSONArray("images").getJSONObject(0).has("url")){

                                    img = events.getJSONObject(i).getJSONArray("images").getJSONObject(0).getString("url")

                                }
                                else{
                                    img = ""
                                }


                            }
                            else{
                                img = ""
                            }



                            timeEvent = if(events.getJSONObject(i).getJSONObject("dates").getJSONObject("start").has("localTime")){
                                events.getJSONObject(i).getJSONObject("dates").getJSONObject("start").getString("localTime")
                            } else{
                                ""
                            }
                            if(events.getJSONObject(i).has("classifications")){

                                if(events.getJSONObject(i).getJSONArray("classifications").getJSONObject(0).has("segment")){

                                    genre = events.getJSONObject(i).getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name")

                                }
                                else{
                                    genre = ""
                                }


                            }
                            else{
                                genre = ""
                            }

                            if(events.getJSONObject(i).has("_embedded")){
                                if(events.getJSONObject(i).getJSONObject("_embedded").has("venues")){

                                    venue =  events.getJSONObject(i).getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name")

                                }
                                else{
                                    venue = ""
                                }

                            }
                            else{
                                venue = ""
                            }

                            var time = ""
                            var _24hr = ""
                            var formatter:DateTimeFormatter
                            var dateTimeEvent = ""
                            var sortTimeDate: LocalDateTime? = null



                            if(timeEvent!== null && timeEvent!==""){

                                val hr = timeEvent.split(":")[0]
                                val mm = timeEvent.split(":")[1]
                                _24hr = "${hr}:${mm}"
                                val _24hrFormat = SimpleDateFormat("HH:mm")
                                val _12hrFormat = SimpleDateFormat("hh:mm a")
                                val _24hrTime = _24hrFormat.parse(_24hr)
                                time = _12hrFormat.format(_24hrTime)

                            }
                            else{
                                time = ""
                                _24hr = ""
                            }

                            if(_24hr === ""){
                                dateTimeEvent = "$dateEvent 00:00"
                                Log.d("datetime",""+dateTimeEvent)
                                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")


                                sortTimeDate = LocalDateTime.parse(dateTimeEvent, formatter)
                                Log.d("sorttimedate when time null:", ""+sortTimeDate)



                            }
                            else{
                                dateTimeEvent = "$dateEvent $_24hr"
                                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                                sortTimeDate = LocalDateTime.parse(dateTimeEvent, formatter)

                            }

                            val yy = dateEvent.split("-")[0]
                            val mm = dateEvent.split("-")[1]
                            val dd = dateEvent.split("-")[2]
                            dateEvent = "$mm/$dd/$yy"
                            Log.d("date format change:",""+dateEvent)

                            val eventObj = Event(name, dateEvent, time,venue,genre,img,sortTimeDate,event_id)
                            eventArr.add(eventObj)






                        }
                        Log.d("before sort:",""+eventArr)
                        eventArr.sortBy { it.date }
                        Log.d("list of events:",""+eventArr)

                        adapter(recyclerView)





                    }else{
                        progress.visibility = View.GONE
                        noEvents.visibility = View.VISIBLE

                    }

                }else{
                    progress.visibility = View.GONE
                    noEvents.visibility = View.VISIBLE

                }



            },
            {
                Log.d("data not received","for events data")

                progress.visibility = View.GONE

                noEvents.visibility = View.VISIBLE

            }
        )
        queue.add(jsonObjectRequest)


    }

    private fun adapter(recyclerView:RecyclerView){


        Log.d("before adapter:",""+eventArr)
        val itemAdapter=TableAdapter(eventArr,requireContext())
        Log.d("adapter finished:", "yes1")


        recyclerView.layoutManager = LinearLayoutManager(context)
        Log.d("adapter finished:", "yes2")

        recyclerView.adapter = itemAdapter
        Log.d("adapter finished:", "yes3")

    }

//    override fun onResume() {
//
//
//        super.onResume()
//
//        val check = view?.findViewById<CheckBox>(R.id.cbHeart)
////        var tag = check?.tag
//        val sharedPreferences = context?.getSharedPreferences("shared preferences",
//            Context.MODE_PRIVATE
//        )
//
//
//        if (sharedPreferences != null) {
//            if (check != null) {
//                check.isChecked = sharedPreferences.contains(name)
//            }
//        }
//    }

    override fun onResume() {
        super.onResume()
//
//        var recyclerView: RecyclerView = requireView().findViewById(R.id.recycler)
//
//        var sharedPreferences = activity?.getSharedPreferences("shared preferences",
//            AppCompatActivity.MODE_PRIVATE
//        )
//        var check = view?.findViewById<CheckBox>(R.id.cbHeart)
//
//
//        for(i in eventArr){
//
//            if (sharedPreferences != null) {
//                for(key in sharedPreferences.all.keys){
//
//                    if (check != null) {
//                        check.isChecked = i.event_id === key
//                    }
//
//
//                }
//            }
//
//
//
//        }
//
//        adapter(recyclerView)


    }



}