package com.volu.volunteerconnect.Model.Events


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Next(
    @SerializedName("limit")
    var limit: Int? = null,
    @SerializedName("page")
    var page: Int? = null
): Parcelable