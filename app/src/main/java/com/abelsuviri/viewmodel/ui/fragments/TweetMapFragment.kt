package com.abelsuviri.viewmodel.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.abelsuviri.data.model.Tweet
import com.abelsuviri.viewmodel.R
import com.abelsuviri.viewmodel.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_tweet_map.*
import kotlinx.android.synthetic.main.fragment_tweet_map.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

class TweetMapFragment : Fragment(), OnMapReadyCallback {

    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var map: GoogleMap
    private val markerList = arrayListOf<Marker>()
    private lateinit var supportMapFragment: SupportMapFragment
    private lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_tweet_map, container, false)
        initMap()
        return rootView
    }

    private fun initMap() {
        supportMapFragment = childFragmentManager.findFragmentById(R.id.resultMapFragment) as SupportMapFragment
        supportMapFragment.getMapAsync(this)
    }

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    private fun initViews() {
        rootView.searchButton.setOnClickListener {
            //Clear all the markers and the map when there is a new request
            markerList.clear()
            map.clear()
            mainViewModel.getTweets(rootView.keywordEditText.text.toString())
            loadingLayout.visibility = View.VISIBLE
            //Hide keyboard automatically after tapping the search button
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(keywordEditText.windowToken, 0)
        }

        map.setOnMarkerClickListener {
            val tweet = it.tag as Tweet
            val action = TweetMapFragmentDirections.actionTweetMapFragmentToTweetDetailsFragment(tweet)
            findNavController().navigate(action)
            true
        }
    }

    private fun subscribeLiveData() {
        mainViewModel.tweetLiveData.observe(this, Observer {
            loadingLayout.visibility = View.GONE

            val coordinates = if (it.geolocation != null) {
                LatLng(it!!.geolocation!!.coordinates[0], it.geolocation!!.coordinates[1])
            } else {
                LatLng(it!!.coordinates!!.coordinates[0], it.coordinates!!.coordinates[1])
            }

            val marker = map.addMarker(MarkerOptions().position(coordinates))
            marker.tag = it     //Set the Tweet object to the marker tag. It will help to remove an specific marker and to show its content
            markerList.add(marker)
            //Move the map to the position of last marker added. To test that markers are deleted after some time the following two lines can be commented
            map.moveCamera(CameraUpdateFactory.newLatLng(coordinates))
            map.animateCamera(CameraUpdateFactory.zoomTo(10f))
        })

        mainViewModel.tweetRemoveLiveData.observe(this, Observer {
            //Remove the markers from the map and from the list of current markers displayed
            val iterator = markerList.listIterator()
            if (iterator.hasNext()) {
                val marker = iterator.next()
                if (marker.tag == it) {
                    marker.remove()
                    markerList.remove(marker)
                }
            }
        })
    }

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        initViews()
        subscribeLiveData()
    }
}
