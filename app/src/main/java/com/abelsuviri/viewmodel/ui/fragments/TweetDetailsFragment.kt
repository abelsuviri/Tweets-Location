package com.abelsuviri.viewmodel.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.abelsuviri.data.model.Tweet
import com.abelsuviri.viewmodel.R
import com.abelsuviri.viewmodel.databinding.FragmentTweetDetailsBinding
import com.squareup.picasso.Picasso

class TweetDetailsFragment : Fragment() {

    private lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val tweet = arguments?.get("tweetDetailsModel") as Tweet
        val binding: FragmentTweetDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_tweet_details, container, false)
        binding.tweet = tweet
        rootView = binding.root

        return rootView
    }
}

@BindingAdapter("imageUrl")
fun loadProfilePicture(imageView: ImageView, url: String) {
    Picasso.get()
        .load(url)
        .fit()
        .into(imageView)
}