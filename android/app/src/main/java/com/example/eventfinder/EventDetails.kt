package com.example.eventfinder

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.eventfinder.adapters.EventAdapter
import com.example.eventfinder.databinding.EventDetailsBinding
import com.facebook.FacebookSdk
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import org.json.JSONObject


val EventToolbarArr = arrayOf(
    "DETAILS",
    "ARTIST(S)",
    "VENUE"
)
val lst = ArrayList<FavEvent>()


val tabIcon = arrayOf(R.drawable.info_icon,R.drawable.artist_icon,R.drawable.venue_icon)



class EventDetails : AppCompatActivity() {

    private lateinit var binding: EventDetailsBinding
    var data: JSONObject = JSONObject()
    var name:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.event_details)






        binding = EventDetailsBinding.inflate(layoutInflater)

//        val view = binding.root

        setContentView(binding.root)

        val bundle = intent.extras

        Log.d("bundle of intent:",""+bundle)

        FacebookSdk.sdkInitialize(applicationContext)








        name = intent.getStringExtra("name").toString()
        var lst = intent.getStringArrayListExtra("event_id")
        val eventId = intent.getStringExtra("event_id")
        Log.d("name of intent:",""+name)

        val viewPager = binding.viewPagerDetails
        val tabLayout = binding.tabLayoutDetails
        val eventName = binding.root.findViewById<TextView>(R.id.eventName)
        eventName.text = name
        eventName.movementMethod = ScrollingMovementMethod()
        eventName.isSelected = true

        val facebookIcon = binding.root.findViewById<ImageView>(R.id.facebook)
        val twitterIcon = binding.root.findViewById<ImageView>(R.id.twitter)

//        Log.d("list lst:",""+lst)

        twitterIcon.setOnClickListener{
            shareOnTwitter()
        }

        facebookIcon.setOnClickListener {
            shareOnFacebook()
        }


        val checkbox2 = binding.root.findViewById<CheckBox>(R.id.cbHeart2)


        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)

        checkbox2.isChecked = sharedPreferences.contains(eventId)

        checkbox2.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {

                if(!sharedPreferences.contains(eventId)){

                    Log.d("sharedPreference:",""+sharedPreferences.all)
                    Log.d("lst before conversion",""+lst)

                    val editor = sharedPreferences.edit()
                    val gson = Gson()
                    val json: String = gson.toJson(lst)

                    Log.d("json string:",""+json)
                    editor.putString(eventId, json)
                    editor.commit()



                    val snack = Snackbar.make(binding.root,"${name} added to favorites",Snackbar.LENGTH_SHORT)
                    val snackbarLayout = snack.view as Snackbar.SnackbarLayout
                    snackbarLayout.setPadding(350, 5, 5, 10)

                    snack.setActionTextColor(Color.parseColor("#FF000000"))
                    snack.view.setBackgroundColor(Color.parseColor("#808080"))

                    snack.show()

                }


            } else {

                if(sharedPreferences.contains(eventId)){

                    sharedPreferences.edit().remove(eventId).commit()
                    Log.d("sharedPreference:",""+sharedPreferences.all)

                    val snack = Snackbar.make(binding.root,"${name} removed from favorites",Snackbar.LENGTH_SHORT)
                    val snackbarLayout = snack.view as Snackbar.SnackbarLayout
                    snackbarLayout.setPadding(350, 5, 5, 10)

                    snack.setActionTextColor(Color.parseColor("#FF000000"))
                    snack.view.setBackgroundColor(Color.parseColor("#808080"))

                    snack.show()



                }




            }
        }





        val queue = Volley.newRequestQueue(this)
        val url = "https://reactticketmaster.wl.r.appspot.com/event_details?event_id=${eventId}"


        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->

                data = response

                val adapter = EventAdapter(supportFragmentManager, lifecycle, data)
                viewPager.adapter = adapter

                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    tab.text = EventToolbarArr[position]
                    tab.setIcon(tabIcon[position])
                }.attach()


                Log.d("data for details response:", "" + response)

            },
            {
                Log.d("data not received","for events")

            }
        )
        queue.add(jsonObjectRequest)







        val backToTable = binding.root.findViewById<ImageButton>(R.id.back_to_table)
        backToTable.setOnClickListener {






            finish()


        }




    }

    private fun shareOnTwitter() {
        val tweetText = "Check out $name on TicketMaster!"
        val tweetUrl = data.getString("url")
        val tweetLink = String.format("https://twitter.com/intent/tweet?text=%s&url=%s",
            Uri.encode(tweetText), Uri.encode(tweetUrl))

// Create a Twitter intent
        val tweetIntent = Intent(Intent.ACTION_VIEW, Uri.parse(tweetLink))

// Launch the Twitter intent on a browser
//        startActivity(Intent.createChooser(tweetIntent, "Share on Twitter"))
        startActivity(tweetIntent)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }


    private fun shareOnFacebook() {
        val shareDialog = ShareDialog(this)
        if (ShareDialog.canShow(ShareLinkContent::class.java)) {
            val content = ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(data.getString("url")))
                .build()
            shareDialog.show(content)
        }
    }






}
