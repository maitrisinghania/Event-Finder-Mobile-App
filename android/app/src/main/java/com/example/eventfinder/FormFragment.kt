package com.example.eventfinder


import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar


var longitude= 0.0
var latitude = 0.0


class FormFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_form, container, false)
    }





    private fun getLocation() {

        Log.d("entered location:", "")


        val location = view?.findViewById<TextView>(R.id.location)
        val queue = Volley.newRequestQueue(context)
        val url = "https://maps.googleapis.com/maps/api/geocode/json?address=${location?.text?.trim()}&key=<Your Key>"

        Log.d("url for location:", ""+url)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->

                Log.d("data received for events response:", "" + response)

                val lenResponse = response.toString().length
                Log.d("data received for events:", "" + lenResponse)

                val resultsArr = response.getJSONArray("results")
                longitude =
                    resultsArr.getJSONObject(0).getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng")
                latitude =
                    resultsArr.getJSONObject(0).getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat")
                Log.d("lng ", "" + longitude)
                Log.d(" lat", "" + latitude)

            },
            {
                Log.d("data not received","for events")

            }
        )
        queue.add(jsonObjectRequest)
    }



    private fun getIpinfo() {
        Log.d("entered ipinfo:", "")

        val queue = Volley.newRequestQueue(context)



        val url = "https://ipinfo.io/?token=8310e76c5e2882"

        Log.d("url for ipinfo:", ""+url)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->

                Log.d("data received for events response:", "" + response)

                val lenResponse = response.toString().length
                Log.d("data received for events:", "" + lenResponse)

                val locStr = response.getString("loc")
                val locArr = locStr.split(",")

                latitude = locArr[0].toDouble()
                longitude = locArr[1].toDouble()



                Log.d("loc array:", "" + locArr)
                Log.d("lng ", "" + longitude)
                Log.d(" lat", "" + latitude)

            },
            {
                Log.d("data not received","for events")

            }
        )
        queue.add(jsonObjectRequest)


    }




    private fun suggest(){

        val suggestions = ArrayList<String>()
        val keywordAuto = view?.findViewById<AutoCompleteTextView>(R.id.autocomplete_keyword)

        val queue = Volley.newRequestQueue(context)
        val url = "https://reactticketmaster.wl.r.appspot.com/suggest?keyword=${keywordAuto?.text?.trim().toString()}"

        Log.d("url :" , ""+url)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->

                Log.d("data received for events response:", "" + response)

                val lenResponse = response.toString().length
                Log.d("data received for events:", "" + lenResponse)

                if(response.has("_embedded") ){
                    if(response.getJSONObject("_embedded").has("attractions")){

                        val embedded = response.getJSONObject("_embedded")
                        val attraction = embedded.getJSONArray("attractions")

                        for (i in 0 until attraction.length())
                            suggestions.add(attraction.getJSONObject(i).getString("name"))

                        val adapter = view?.let {
                            ArrayAdapter(
                                it.context,
                                android.R.layout.simple_list_item_1,
                                suggestions
                            )
                        }
                        if (keywordAuto != null) {
                            keywordAuto.threshold = 0
                            keywordAuto.setAdapter(adapter)
                        }

                    }
                }


            },
            {
                Log.d("data not received","for events")

            }
        )
        queue.add(jsonObjectRequest)

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        val spinner = view.findViewById<Spinner>(R.id.spinner)
        val location = view.findViewById<TextView>(R.id.location)
        val switch = view.findViewById<SwitchCompat>(R.id.switchActivated)

        val searchButton = view.findViewById(R.id.search_btn) as Button
        val clearButton = view.findViewById<Button>(R.id.clear_btn)
        val keyword = view.findViewById<AutoCompleteTextView>(R.id.autocomplete_keyword)
        val radius = view.findViewById<EditText>(R.id.distance)
        val categories = arrayOf<String?>("All", "Music", "Sports", "Arts & Theatre", "Fim", "Miscellaneous")
        val arrayAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(view.context, R.layout.spinner_text, categories)
        arrayAdapter.setDropDownViewResource(R.layout.spinner_text)
        spinner.adapter = arrayAdapter

        Log.d("keyword before bundle:",""+keyword)

        Log.d("bundle before:",""+arguments)

        val b = arguments
        Log.d("b bundle:",""+b)



        if (b !== null){

            switch.isChecked = b.getBoolean("switch")
            Log.d("switch after bundle:",""+switch)
            if(switch.isChecked) {
                location!!.visibility = View.GONE
            }
            else{
                location!!.text = b.getString("location")
                Log.d("location after bundle:",""+location)

                location.visibility = View.VISIBLE


            }

            val position = b.getInt("spinner")
            spinner.setSelection(position)

            Log.d("spinner after bundle:",""+spinner)
            Log.d("spinner:", ""+spinner.selectedItemPosition)



            keyword.setText(b.getString("keyword"))
            Log.d("keyword after bundle:",""+keyword)



            radius.setText(b.getInt("radius").toString())
            Log.d("radius after bundle:",""+radius)




        }
        else{

            keyword.setText("")
            spinner.setSelection(0)

            radius.setText("10")
            location!!.text = ""
            switch.isChecked = false

        }




        keyword.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                suggest()
            }
            override fun afterTextChanged(s: Editable?) {

            }
        })







        switch?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)

                location.visibility = View.GONE
            else
                location.visibility = View.VISIBLE
        }


        clearButton.setOnClickListener {
            keyword.setText("")
            spinner.setSelection(0)

            radius.setText("10")
            location.text = ""
            switch.isChecked = false



        }





        searchButton.setOnClickListener {

            val bundle = Bundle()

            var category = ""

            val check = switch.isChecked

            if(spinner.selectedItemPosition === 0){
                category = ""
            }else if(spinner.selectedItemPosition === 1){
                category = "KZFzniwnSyZfZ7v7nJ"

            }else if(spinner.selectedItemPosition === 2){
                category = "KZFzniwnSyZfZ7v7nE"
            }else if(spinner.selectedItemPosition === 3){
                category = "KZFzniwnSyZfZ7v7na"
            }else if(spinner.selectedItemPosition === 4){
                category = "KZFzniwnSyZfZ7v7nn"
            }else if(spinner.selectedItemPosition === 5){
                category = "KZFzniwnSyZfZ7v7n1"
            }


            bundle.putString("keyword",keyword.text.toString())
            bundle.putString("category",category)
            bundle.putDouble("latitude", latitude)
            bundle.putDouble("longitude", longitude)
            bundle.putInt("radius",radius.text.toString().toInt())
            bundle.putBoolean("switch",check)
            bundle.putString("location",location.text.toString())
            bundle.putInt("spinner",spinner.selectedItemPosition)

            Log.d("spinner:", ""+spinner.selectedItemPosition)

            Log.d("bundle in form:",""+bundle)

            if(keyword.text.toString().isNotEmpty() ) {
                Log.d("keyword.text.trim()"+keyword.text.trim(),""+keyword.text.toString())
                if (switch.isChecked) {
                    getIpinfo()

                    Navigation.findNavController(view).navigate(R.id.navigateToTableFragment, bundle )

                } else {
                    if (location.text.isNotEmpty()) {
                        getLocation()
                        Navigation.findNavController(view).navigate(R.id.navigateToTableFragment,bundle)
                    } else {
                        val snack = Snackbar.make(it,"Please fill all fields",Snackbar.LENGTH_SHORT)
                        val snackbarLayout = snack.view as Snackbar.SnackbarLayout

                        // set padding of the all corners as 0
                        snackbarLayout.setPadding(350, 5, 5, 10)

                        snack.setActionTextColor(Color.parseColor("#FF000000"))
                        snack.view.setBackgroundColor(Color.parseColor("#808080"))

                        snack.show()


                    }
                }


            }else{
                val snack = Snackbar.make(it,"Please fill all fields",Snackbar.LENGTH_SHORT)

                val snackbarLayout = snack.view as Snackbar.SnackbarLayout

                // set padding of the all corners as 0
                snackbarLayout.setPadding(350, 5, 5, 10)

                snack.setActionTextColor(Color.parseColor("#FF000000"))
                snack.view.setBackgroundColor(Color.parseColor("#808080"))
                snack.show()

            }

        }



    }



}