package com.volu.volunteerconnect.Model.User


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserData(
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("_id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("phoneNumber")
    val phoneNumber: String?,
    @SerializedName("photos")
    val photos: String?,
    @SerializedName("resetPasswordExpire")
    val resetPasswordExpire: String?,
    @SerializedName("resetPasswordToken")
    val resetPasswordToken: String?,
    @SerializedName("role")
    val role: String?,
    @SerializedName("username")
    val username: String?,
    @SerializedName("__v")
    val v: Int?
) : Parcelable