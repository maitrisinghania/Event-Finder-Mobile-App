package com.example.eventfinder.fragments

import ArtistsAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.eventfinder.R
import com.example.eventfinder.TableAdapter
import com.example.eventfinder.TableFragment
import com.example.eventfinder.databinding.FragmentArtistBinding
import com.google.android.material.progressindicator.CircularProgressIndicator
import org.json.JSONObject
import java.time.LocalDateTime


class ArtistFragment(private val data:JSONObject) : Fragment() {
    private var _binding: FragmentArtistBinding? = null
    private val binding get() = _binding!!


    data class ArtistsList(var name: String, var popularity: Int, var followers: String , var img: String, var spotify: String, var albums: ArrayList<String> = ArrayList())
    var artistsDetails = ArrayList<ArtistsList>()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentArtistBinding.inflate(inflater, container, false)

        val view = binding.root

        val recyclerView: RecyclerView =view.findViewById(R.id.recyclerArtist)
        val progress = view.findViewById<ProgressBar>(R.id.progressMusic)
        val noArtist = view.findViewById<TextView>(R.id.noMusic)
        val itemAdapter= ArtistsAdapter(artistsDetails,requireContext())


        var artistArr = ArrayList<String>()
        val attractions = data.getJSONObject("_embedded").getJSONArray("attractions")
        for(i in 0 until attractions.length()){

            artistArr.add(attractions.getJSONObject(i).getString("name"))
        }



        for(i in 0 until artistArr.size){


            val queue = Volley.newRequestQueue(context)
            val url = "https://reactticketmaster.wl.r.appspot.com/spotify?name=${artistArr[i].trim()}"


            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    Log.d("data for artist response:", "" + response)


                    var artistData = response
                    if(artistData.has("error")){

                        progress.visibility = View.GONE
                        noArtist.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE

                    }
                    else{
                        if(artistData.getString("name").equals(artistArr[i].trim()) ){

                            Log.d("entered artist name correct:",""+artistData.getString("name"))
                            Log.d("artistname:",""+artistArr[i].trim())



                            var artistName = artistData.getString("name")
                            var artistPopularity = artistData.getInt("popularity")
                            var id = artistData.getString("id")
                            var link = artistData.getJSONObject("external_urls").getString("spotify")
                            var artistImg = artistData.getJSONArray("images").getJSONObject(0).getString("url")
                            var artistFollowers = artistData.getJSONObject("followers").getInt("total")
                            var convertedFollowers = convertNumberToFormattedString(artistFollowers)


                            var albumImg:ArrayList<String> = ArrayList()

                            val queueArtist = Volley.newRequestQueue(context)
                            val urlArtist = "https://reactticketmaster.wl.r.appspot.com/spotify_albums?id=${id}"

                            Log.d("bfore albums:",""+urlArtist)


                            val jsonArrayRequest = JsonArrayRequest(
                                Request.Method.GET, urlArtist, null,
                                { response ->
                                    Log.d("data for artist response:", "" + response)


                                    for(i in 0 until response.length()){

                                        albumImg.add(response.getJSONObject(i).getJSONArray("images").getJSONObject(0).getString("url"))
                                    }


                                    val artistObj = ArtistsList(artistName,artistPopularity,convertedFollowers,artistImg,link,albumImg)
                                    artistsDetails.add(artistObj)

                                    itemAdapter.notifyDataSetChanged()

                                    Log.d("artistsDetails:",""+artistsDetails)



                                },
                                {
                                    Log.d("data not received","for events")
                                }
                            )
                            queueArtist.add(jsonArrayRequest)






                        }else{

                            progress.visibility = View.GONE

                            noArtist.visibility = View.VISIBLE
                            recyclerView.visibility = View.GONE

                        }

                    }

                },
                {
                    Log.d("data not received","for events")

                }
            )
            queue.add(jsonObjectRequest)





        }

        progress.visibility = View.GONE


        Log.d("artistsDetails before check:",""+artistsDetails)
        if(artistsDetails!==null){

            Log.d("before adapter:",""+artistsDetails)

            Log.d("adapter finished:", "yes1")


            recyclerView.layoutManager = LinearLayoutManager(context)
            Log.d("adapter finished:", "yes2")

            recyclerView.adapter = itemAdapter
            Log.d("adapter finished:", "yes3")
        }
        else{

            noArtist.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }




















        return view
    }



    private fun convertNumberToFormattedString(number: Int): String {
        val doubleNumber = number.toDouble()
        val million = doubleNumber / 1000000
        val thousand = (doubleNumber - million * 1000000) / 1000
        val rest = doubleNumber - million * 1000000 - thousand * 1000

        return when {
            million >= 1 -> "${String.format("%.0f", million)}M Followers"
            thousand >= 1 -> "${String.format("%.0f", thousand)}K Followers"
            else -> "${String.format("%.0f", rest)} Followers"
        }
    }








}