package com.abelsuviri.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author Abel Suviri
 */

@Parcelize
data class User(
    @SerializedName("name") val name: String,
    @SerializedName("verified") val verified: Boolean,
    @SerializedName("profile_image_url_https") val profilePicture: String
): Parcelable