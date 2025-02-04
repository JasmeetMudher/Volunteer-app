package com.volu.volunteerconnect.Utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.volu.volunteerconnect.Model.Auth.ErrorResponse
import retrofit2.Response

fun <T> errorResponse(response: Response<T>): ErrorResponse? {
    val gson = Gson()
    val type = object : TypeToken<ErrorResponse?>() {}.type
    return gson.fromJson(response.errorBody()!!.charStream(), type)
}