package com.example.eventfinder


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import com.example.eventfinder.adapters.MyPagerAdapter
import com.example.eventfinder.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

val ToolbarArr = arrayOf(
    "Search",
    "Favorites"
)


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.Theme_EventFinder)
        super.onCreate(savedInstanceState)
//        SingletonSharePreference.with(application)



//        val bundle = intent.extras
//        val check = intent.getStringExtra("check")
//        if(bundle!== null && check.equals("true") ){
//
//
//
//        }



        binding = ActivityMainBinding.inflate(layoutInflater)
//        val view = binding.root

        setContentView(binding.root)

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        val adapter = MyPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = ToolbarArr[position]
        }.attach()


    }

}