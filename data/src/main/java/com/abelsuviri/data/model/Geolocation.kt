package com.abelsuviri.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author Abel Suviri
 */

@Parcelize
data class Geolocation (
    @SerializedName("coordinates") val coordinates: List<Double> = arrayListOf()
): Parcelable