package com.example.eventfinder.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.eventfinder.fragments.ArtistFragment
import com.example.eventfinder.fragments.DetailsFragment

import com.example.eventfinder.fragments.VenueFragment
import org.json.JSONObject

private const val NUM_TABS = 3

class EventAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle, private var data:JSONObject) :
    FragmentStateAdapter(fragmentManager, lifecycle) {



    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            2 -> return VenueFragment(data)
            1 -> return ArtistFragment(data)

        }
        return DetailsFragment(data)
    }
}