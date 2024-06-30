package com.example.eventfinder.fragments

import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.eventfinder.R
import com.example.eventfinder.databinding.FragmentDetailsBinding
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class DetailsFragment(private val data:JSONObject) : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        var artistText = view.findViewById<TextView>(R.id.text_artist)
        var venueText = view.findViewById<TextView>(R.id.text_venue)
        var dateText = view.findViewById<TextView>(R.id.text_date)
        var timeText = view.findViewById<TextView>(R.id.text_time)
        var genresText = view.findViewById<TextView>(R.id.text_genres)
        var priceText = view.findViewById<TextView>(R.id.text_price)
        var statusText = view.findViewById<TextView>(R.id.text_status)
        var buyAtText = view.findViewById<TextView>(R.id.text_buyAt)
        var artistEdit = view.findViewById<TextView>(R.id.edit_artist)
        var venueEdit = view.findViewById<TextView>(R.id.edit_venue)
        var dateEdit = view.findViewById<TextView>(R.id.edit_date)
        var timeEdit = view.findViewById<TextView>(R.id.edit_time)
        var genresEdit = view.findViewById<TextView>(R.id.edit_genres)
        var priceEdit = view.findViewById<TextView>(R.id.edit_price)
        var statusEdit = view.findViewById<TextView>(R.id.edit_status)
        var buyAtEdit = view.findViewById<TextView>(R.id.edit_buyAt)
        var statusCard = view.findViewById<CardView>(R.id.statusCard)

        artistEdit.isSelected = true
        artistEdit.movementMethod = ScrollingMovementMethod()

        venueEdit.isSelected = true
        venueEdit.movementMethod = ScrollingMovementMethod()

        dateEdit.isSelected = true
        dateEdit.movementMethod = ScrollingMovementMethod()

        timeEdit.isSelected = true
        timeEdit.movementMethod = ScrollingMovementMethod()

        genresEdit.isSelected = true
        genresEdit.movementMethod = ScrollingMovementMethod()

        priceEdit.isSelected = true
        priceEdit.movementMethod = ScrollingMovementMethod()

        buyAtEdit.isSelected = true
        buyAtEdit.movementMethod = ScrollingMovementMethod()




        var seatmap = view.findViewById<ImageView>(R.id.seatmap)
        var artistArr = ArrayList<String>()
        var genresArr = ArrayList<String>()

        Log.d("data in data:",""+data)







                if(data.has("_embedded")){
                    if(data.getJSONObject("_embedded").has("attractions")){

                        val attractions = data.getJSONObject("_embedded").getJSONArray("attractions")
                        for(i in 0 until attractions.length()){

                            artistArr.add(attractions.getJSONObject(i).getString("name"))
                        }

                        val temp = artistArr.joinToString(" | ")
                        artistEdit.text = temp



                    }
                    else{

                        artistText.visibility = View.GONE
                        artistEdit.visibility = View.GONE

                    }

                    if(data.getJSONObject("_embedded").has("venues")){

                        var venue = data.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name")

                        venueEdit.text = venue


                    }
                    else{
                        venueEdit.visibility = View.GONE
                        venueText.visibility = View.GONE
                    }



                }
                else{
                    artistText.visibility = View.GONE
                    artistEdit.visibility = View.GONE
                    venueEdit.visibility = View.GONE
                    venueText.visibility = View.GONE
                }

                if(data.has("dates")){
                    if(data.getJSONObject("dates").getJSONObject("start").has("localDate")){
                        var date = data.getJSONObject("dates").getJSONObject("start").getString("localDate")
                        val local = LocalDate.of(date.split("-")[0].toInt(), date.split("-")[1].toInt(), date.split("-")[2].toInt())
                        var dateFormatted = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(local)

                        dateEdit.text = dateFormatted.toString()

                    }
                    else{
                        dateEdit.visibility = View.GONE
                        dateText.visibility = View.GONE

                    }

                    if(data.getJSONObject("dates").getJSONObject("start").has("localTime")){


                        var time = data.getJSONObject("dates").getJSONObject("start").getString("localTime")
                        val hr = time.split(":")[0]
                        val mm = time.split(":")[1]
                        var _24hr = "${hr}:${mm}"
                        val _24hrFormat = SimpleDateFormat("HH:mm")
                        val _12hrFormat = SimpleDateFormat("hh:mm a")
                        val _24hrTime = _24hrFormat.parse(_24hr)
                        var timeFormatted = _12hrFormat.format(_24hrTime)
                        timeEdit.text = timeFormatted.toString()


                    }
                    else{
                        timeEdit.visibility = View.GONE
                        timeText.visibility = View.GONE
                    }

                    if(data.getJSONObject("dates").has("status")){

                        var status = data.getJSONObject("dates").getJSONObject("status").getString("code")



                        if(status.equals("onsale") ){
                            statusEdit.text = "On Sale"

                            statusCard.setCardBackgroundColor(Color.rgb(50, 205, 50))

                        }
                        else if(status.equals("offsale")){

                            statusEdit.text = "Off Sale"
                            statusCard.setCardBackgroundColor(Color.RED)

                        }
                        else if(status.equals("canceled")){

                            statusEdit.text = "Canceled"
                            statusCard.setCardBackgroundColor(Color.BLACK)

                        }
                        else if(status.equals("postponed")){

                            statusEdit.text = "Postponed"
                            statusCard.setCardBackgroundColor(Color.rgb(244, 187, 68))

                        }
                        else if(status.equals("rescheduled")){

                            statusEdit.text = "Rescheduled"
                            statusCard.setCardBackgroundColor(Color.rgb(244, 187, 68))

                        }
                        else{

                            statusEdit.visibility = View.GONE
                            statusText.visibility = View.GONE
                            statusCard.visibility = View.GONE

                        }


                    }
                }
                else{
                    timeEdit.visibility = View.GONE
                    timeText.visibility = View.GONE
                    dateEdit.visibility = View.GONE
                    dateText.visibility = View.GONE
                    statusEdit.visibility = View.GONE
                    statusText.visibility = View.GONE
                    statusCard.visibility = View.GONE

                }

                if(data.has("classifications")){


                    var classifications = data.getJSONArray("classifications").getJSONObject(0)

                    if(classifications.has("genre")){
                        genresArr.add(classifications.getJSONObject("genre").getString("name"))
                    }

                    if(classifications.has("segment")){
                        genresArr.add(classifications.getJSONObject("segment").getString("name"))
                    }

                    if(classifications.has("subGenre")){
                        genresArr.add(classifications.getJSONObject("subGenre").getString("name"))
                    }

                    if(classifications.has("type")){
                        genresArr.add(classifications.getJSONObject("type").getString("name"))
                    }

                    if(classifications.has("subType")){
                        genresArr.add(classifications.getJSONObject("subType").getString("name"))
                    }

                    if(genresArr!==null){

                        val temp = genresArr.joinToString(" | ")

                        genresEdit.text =temp
                    }
                    else{

                        genresEdit.visibility = View.GONE
                        genresText.visibility = View.GONE
                    }




                }
                else{
                    genresEdit.visibility = View.GONE
                    genresText.visibility = View.GONE
                }

                if(data.has("priceRanges")){

                    var priceArr = ArrayList<String>()
                    var priceRanges =data.getJSONArray("priceRanges").getJSONObject(0)

                    if(priceRanges.has("min")){

                       priceArr.add(priceRanges.getString("min"))
                    }
                    if(priceRanges.has("max")){
                        priceArr.add(priceRanges.getString("max"))
                    }

                    if(priceArr!==null && priceArr.size < 2){

                        var temp = priceArr[0]
                        priceEdit.text = "$temp (USD)"
                    }
                    else{

                        var min = priceRanges.getString("min")
                        var max = priceRanges.getString("max")
                        var price = "$min - $max (USD)"
                        priceEdit.text = price

                    }

                }
                else{

                    priceEdit.visibility = View.GONE
                    priceText.visibility = View.GONE
                }

                if(data.has("url")){
                    buyAtEdit.text = data.getString("url")
                }
                else{
                    buyAtText.visibility = View.GONE
                    buyAtEdit.visibility = View.GONE
                }

                if(data.has("seatmap")){

                    val url = data.getJSONObject("seatmap").getString("staticUrl")

                    Glide.with(this).load(url).into(seatmap)

                }
                else{
                    seatmap.visibility = View.GONE
                }


































        return view
    }




}