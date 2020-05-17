package com.abelsuviri.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author Abel Suviri
 */

@Parcelize
data class Tweet(
    @SerializedName("text") val content: String,
    @SerializedName("user") val user: User,
    @SerializedName("geo") val geolocation: Geolocation?,
    @SerializedName("coordinates") val coordinates: Geolocation?,
    @SerializedName("retweet_count") val reTweets: Int,
    @SerializedName("favorite_count") val favourites: Int
): Parcelable