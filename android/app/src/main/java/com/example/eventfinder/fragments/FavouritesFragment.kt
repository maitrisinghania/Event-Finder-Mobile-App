package com.example.eventfinder.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventfinder.FavAdapter
import com.example.eventfinder.FavEvent
import com.example.eventfinder.R
import com.example.eventfinder.TableAdapter
import com.example.eventfinder.TableFragment
import com.example.eventfinder.databinding.FragmentFavouritesBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.util.Objects
import kotlin.reflect.typeOf


class FavouritesFragment : Fragment() {

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!
    val gson = Gson()

    data class Lst(var name: String, var venue: String, var genre: String, var date: String, var time: String, var img: String, var id:String)

    var eventArrFav = ArrayList<String>()
    var lstFav = ArrayList<Lst>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
//
        val view = binding.root

//        val noFav = view.findViewById<TextView>(R.id.noFav)
//
//
//        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_fav)
//
//        val sharedPreferences = context?.getSharedPreferences("shared preferences",
//            Context.MODE_PRIVATE
//        )
//
//        val size = sharedPreferences?.all?.size
//
//
//
//        if (sharedPreferences != null && size !==0) {
//
//            for(key in sharedPreferences.all.keys){
//
//                val jsonString = sharedPreferences.getString(key, null)
//                Log.d("jsonString in fav:",""+jsonString)
//                if (jsonString != null) {
//
//                    Log.d("jsonString at 0:",""+jsonString[0])
//                }
//
//                val type: Type = object : TypeToken<ArrayList<String>?>() {}.type
//
//                // in below line we are getting data from gson
//                // and saving it to our array list
//                eventArrFav = gson.fromJson<Any>(jsonString, type) as ArrayList<String>
//                Log.d("object in fav:",""+eventArrFav)
//
//                val name = eventArrFav[0]
//                val venue = eventArrFav[1]
//                val genre = eventArrFav[2]
//                val date = eventArrFav[3]
//                val time = eventArrFav[4]
//                val img = eventArrFav[5]
//                val id = eventArrFav[6]
//
//                Log.d("eventArrFav parts:",""+name)
//
//                val obj = Lst(name,venue,genre,date,time,img,id)
//                lstFav.add(obj)
//
//
//            }
//
//
//
//
//
//
////
//            noFav.visibility = View.GONE
//
//
//
//
//
//
//            adapter(recyclerView)
//
//
//
//
//
//
//
//        }
//        else{
//
//            noFav.visibility = View.VISIBLE
//
//        }
//
//
//
//

        return binding.root
    }


    private fun adapter(recyclerView: RecyclerView){


        Log.d("before adapter:",""+lstFav)
        val itemAdapter= FavAdapter(lstFav,requireContext())
        Log.d("adapter finished:", "yes1")


        recyclerView.layoutManager = LinearLayoutManager(context)
        Log.d("adapter finished:", "yes2")

        recyclerView.adapter = itemAdapter
        Log.d("adapter finished:", "yes3")

    }

    override fun onResume() {
        super.onResume()

        val recyclerView: RecyclerView = binding.root.findViewById(R.id.recycler_fav)
        val nameFav = view?.findViewById<TextView>(R.id.fav_name)
        val name = nameFav?.text
        recyclerView.visibility = View.VISIBLE


        val view = binding.root

        val noFav = view.findViewById<TextView>(R.id.noFav)




        val sharedPreferences = context?.getSharedPreferences("shared preferences",
            Context.MODE_PRIVATE
        )

        val size = sharedPreferences?.all?.size
        lstFav.clear()





        if (sharedPreferences != null && size !==0) {

            for(key in sharedPreferences.all.keys){

                val jsonString = sharedPreferences.getString(key, "")
                Log.d("jsonString in fav:",""+jsonString)
                if (jsonString != null) {

                    Log.d("jsonString at 0:",""+jsonString[0])
                }

                val type: Type = object : TypeToken<ArrayList<String>?>() {}.type

                // in below line we are getting data from gson
                // and saving it to our array list



                var eventArr =gson.fromJson<Any>(jsonString, type) as ArrayList<String>
                Log.d("object in fav:",""+eventArr)
                eventArrFav = ArrayList(eventArr)

                val name = eventArrFav[0]
                val venue = eventArrFav[1]
                val genre = eventArrFav[2]
                val date = eventArrFav[3]
                val time = eventArrFav[4]
                val img = eventArrFav[5]
                val id = eventArrFav[6]

                Log.d("eventArrFav parts:",""+name)




                val obj = Lst(name,venue,genre,date,time,img,id)
                lstFav.add(obj)


            }
            noFav.visibility = View.GONE
            adapter(recyclerView)
        }
        else{

            noFav.visibility = View.VISIBLE

        }


//
//
//

//        val checkboxFav = view?.findViewById<CheckBox>(R.id.fav_cbHeart)



//        checkboxFav?.setOnClickListener {
//
//            if (sharedPreferences != null) {
//                recyclerView.visibility = View.VISIBLE
//                for(i in sharedPreferences.all.keys){
//
//                    val jsonString = sharedPreferences?.getString(i, null)
//                    Log.d("jsonString in fav:",""+jsonString)
//                    if (jsonString != null) {
//
//                        Log.d("jsonString at 0:",""+jsonString[0])
//                    }
//
//                    val type: Type = object : TypeToken<ArrayList<String>?>() {}.type
//
//
//                    eventArrFav = gson.fromJson<Any>(jsonString, type) as ArrayList<String>
//                    Log.d("object in fav:",""+eventArrFav)
//
//
//                    if(eventArrFav[0] === name){
//
//                        if (sharedPreferences != null) {
//                            sharedPreferences.edit().remove(i).commit()
//                        }
//
//                    }
//
//
//                }
//            }
//            else{
//
//                if (noFav != null) {
//                    noFav.visibility = View.VISIBLE
//                }
//                recyclerView.visibility = View.GONE
//
//            }
//
//
//        }











        val gson = Gson()


        if (sharedPreferences != null && size !==0) {
            recyclerView.visibility = View.VISIBLE


            for(i in sharedPreferences.all.keys){

                    val jsonString = sharedPreferences?.getString(i, null)
                    Log.d("jsonString in fav:",""+jsonString)
                    if (jsonString != null) {

                        Log.d("jsonString at 0:",""+jsonString[0])
                    }

                    val type: Type = object : TypeToken<ArrayList<String>?>() {}.type


                    eventArrFav = gson.fromJson<Any>(jsonString, type) as ArrayList<String>
                    Log.d("object in fav:",""+eventArrFav)


                    if(eventArrFav[0] === name){

                        if (sharedPreferences != null) {
                            sharedPreferences.edit().remove(i).commit()

                        }

                    }


                }




            if (noFav != null) {
                noFav.visibility = View.GONE
            }

            adapter(recyclerView)




        }
        else{

            if (noFav != null) {
                noFav.visibility = View.VISIBLE
            }
            recyclerView.visibility = View.INVISIBLE

        }



    }






}