package com.example.eventfinder


import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Color

import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class TableAdapter(private val eventList: ArrayList<TableFragment.Event>, private var context: Context) : RecyclerView.Adapter<TableAdapter.MyViewHolder>() {



    var lst: ArrayList<FavEvent> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {



        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_card, parent, false)





        return MyViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        return eventList.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = eventList[position]


        val obj = FavEvent(event.name,event.venue,event.genre,event.date,event.time,event.img,event.event_id)
        lst.add(obj)

        var lst1 = ArrayList<String>()
        lst1.add(0,event.name)
        lst1.add(1,event.venue)
        lst1.add(2,event.genre)
        lst1.add(3,event.date)
        lst1.add(4,event.time)
        lst1.add(5,event.img)
        lst1.add(6,event.event_id)
        Log.d("json array before string:",""+lst1)


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
        holder.checkbox.tag = event.event_id

        holder.checkbox.isChecked = sharedPreferences.contains(event.event_id)










        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {

                if(!sharedPreferences.contains(event.event_id)){

                    Log.d("sharedPreference:",""+sharedPreferences.all)
                    Log.d("lst before conversion",""+lst1)

                    val editor = sharedPreferences.edit()
                    val gson = Gson()
                    val json: String = gson.toJson(lst1)

                    Log.d("json string:",""+json)
                    editor.putString(event.event_id, json)
                    editor.commit()
//                    holder.recyclerView.visibility = View.VISIBLE



                    val snack = Snackbar.make(holder.itemView.rootView,"${event.name} added to favorites",Snackbar.LENGTH_SHORT)
                    val snackbarLayout = snack.view as Snackbar.SnackbarLayout
                    snackbarLayout.setPadding(5, 5, 5, 10)

                    snack.setActionTextColor(Color.parseColor("#FF000000"))
                    snack.view.setBackgroundColor(Color.parseColor("#808080"))

                    snack.show()

                }


            } else {

                if(sharedPreferences.contains(event.event_id)){

                    sharedPreferences.edit().remove(event.event_id).commit()
                    Log.d("sharedPreference:",""+sharedPreferences.all)

                    val snack = Snackbar.make(holder.itemView.rootView,"${event.name} removed from favorites",Snackbar.LENGTH_SHORT)
                    val snackbarLayout = snack.view as Snackbar.SnackbarLayout
                    snackbarLayout.setPadding(5, 5, 5, 10)

                    snack.setActionTextColor(Color.parseColor("#FF000000"))
                    snack.view.setBackgroundColor(Color.parseColor("#808080"))

                    snack.show()



                }




            }
        }







        holder.imgLayout.setOnClickListener {

            val intent = Intent(this@TableAdapter.context, EventDetails::class.java)
            intent.putExtra("name",event.name)
            intent.putExtra("event_id",event.event_id)
            intent.putStringArrayListExtra(event.name,lst1)


            context.startActivity(intent)


        }
        holder.middleLayout.setOnClickListener {


            val intent = Intent(this@TableAdapter.context, EventDetails::class.java)
            intent.putExtra("name",event.name)
            intent.putExtra("event_id",event.event_id)
            intent.putStringArrayListExtra(event.name,lst1)


            context.startActivity(intent)
        }











        Glide.with(context)
            .load(event.img)
            .into(holder.img)




    }

    // This class defines the ViewHolder object for each item in the RecyclerView
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {

        val name: TextView = itemView.findViewById(R.id.name)
        val venue: TextView = itemView.findViewById(R.id.venue)
        val genre: TextView = itemView.findViewById(R.id.genre)
        val date: TextView = itemView.findViewById(R.id.date)
        val time: TextView = itemView.findViewById(R.id.time)
        val img : ImageView = itemView.findViewById(R.id.events_img)
        val imgLayout: CardView = itemView.findViewById(R.id.img_layout)
        val middleLayout: LinearLayout = itemView.findViewById(R.id.middle_layout)
        val checkbox = itemView.findViewById<CheckBox>(R.id.cbHeart)
//        val recyclerView = itemView.findViewById<RecyclerView>(R.id.recycler)







    }







}

