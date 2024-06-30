package com.example.eventfinder


import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Color
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventfinder.fragments.FavouritesFragment
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class FavAdapter(private val eventList: ArrayList<FavouritesFragment.Lst>, private var context: Context) : RecyclerView.Adapter<FavAdapter.MyViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {



        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_favorite, parent, false)





        return MyViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        return eventList.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = eventList[position]


        val sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE)


        holder.name.isSelected = true
        holder.name.movementMethod = ScrollingMovementMethod()
        holder.venue.isSelected = true
        holder.venue.movementMethod = ScrollingMovementMethod()
        holder.name.text = event.name
        holder.venue.text = event.venue
        holder.genre.text = event.genre
        holder.date.text = event.date
        holder.time.text = event.time


        Glide.with(context)
            .load(event.img)
            .into(holder.img)

        holder.checkbox.isChecked = true

        if(sharedPreferences == null){

            holder.noFav.visibility = View.VISIBLE




        }
        else{
            holder.favCard.visibility = View.VISIBLE
        }








        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->


            holder.favCard.visibility = View.GONE








                    for(i in sharedPreferences.all.keys){

                        val jsonString = sharedPreferences?.getString(i, null)
                        Log.d("jsonString in fav:",""+jsonString)
                        if (jsonString != null) {

                            Log.d("jsonString at 0:",""+jsonString[0])
                        }

                        val type: Type = object : TypeToken<ArrayList<String>?>() {}.type


                        val eventArrFav = Gson().fromJson<Any>(jsonString, type) as ArrayList<String>
                        Log.d("object in fav:",""+eventArrFav)


                        if(eventArrFav[0] === holder.name.text){

                            sharedPreferences?.edit()?.remove(i)?.commit()
                            holder.checkbox.isChecked = false



                        }
                        if(sharedPreferences == null){

                            holder.noFav.visibility = View.VISIBLE


                        }










//










                    val snack = Snackbar.make(holder.itemView.rootView,"${event.name} removed from favorites",Snackbar.LENGTH_SHORT)
                    val snackbarLayout = snack.view as Snackbar.SnackbarLayout
                    snackbarLayout.setPadding(5, 5, 5, 10)

                    snack.setActionTextColor(Color.parseColor("#FF000000"))
                    snack.view.setBackgroundColor(Color.parseColor("#808080"))

                    snack.show()




            }





        }
























    }

    // This class defines the ViewHolder object for each item in the RecyclerView
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {

        val name: TextView = itemView.findViewById(R.id.fav_name)
        val venue: TextView = itemView.findViewById(R.id.fav_venue)
        val genre: TextView = itemView.findViewById(R.id.fav_genre)
        val date: TextView = itemView.findViewById(R.id.fav_date)
        val time: TextView = itemView.findViewById(R.id.fav_time)
        val img : ImageView = itemView.findViewById(R.id.fav_events_img)
        val favCard = itemView.findViewById<CardView>(R.id.fav_layout_card)
        val checkbox = itemView.findViewById<CheckBox>(R.id.fav_cbHeart)
        val noFav = itemView.findViewById<TextView>(R.id.noFav)






    }





}