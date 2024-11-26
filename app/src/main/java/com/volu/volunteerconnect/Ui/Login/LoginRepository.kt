package com.volu.volunteerconnect.Ui.Login

import android.content.Context
import com.volu.volunteerconnect.Model.Auth.LoginData
import com.volu.volunteerconnect.API.VolunteerConnectApi
import com.volu.volunteerconnect.Model.Auth.Login
import com.volu.volunteerconnect.R
import com.volu.volunteerconnect.Utils.ResponseState
import com.volu.volunteerconnect.Utils.errorResponse
import com.volu.volunteerconnect.Utils.hasInternetConnection
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(
    private val api: VolunteerConnectApi,
    @ApplicationContext private val context: Context
) {

    suspend fun login(loginData: LoginData): ResponseState<Login?> {
        if (!context.hasInternetConnection()) {
            return ResponseState.Error(context.getString(R.string.error_internet_turned_off))
        }
        val response = try {
            api.loginUser(loginData)
        } catch (e: HttpException) {
            return ResponseState.Error(context.getString(R.string.error_http))
        } catch (e: IOException) {
            return ResponseState.Error(context.getString(R.string.check_internet_connection))
        }

        return if (response.isSuccessful && response.body()?.success == true) {
            ResponseState.Success(response.body()!!)
        } else {
            ResponseState.Error(errorResponse(response)?.error.toString())
        }

    }
}