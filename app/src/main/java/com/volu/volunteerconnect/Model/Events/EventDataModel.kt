package com.volu.volunteerconnect.Model.Events


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventDataModel(
    @SerializedName("data")
    var data: DataFetch? = null,
    @SerializedName("success")
    var success: Boolean? = null
): Parcelable