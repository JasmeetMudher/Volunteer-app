package com.volu.volunteerconnect.Ui.ForgotPassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volu.volunteerconnect.Model.Auth.LoginData
import com.volu.volunteerconnect.Model.Auth.Login
import com.volu.volunteerconnect.Utils.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val fpRepository: ForgotPasswordRepository
): ViewModel() {

    private val _fpDataLiveData = MutableLiveData<ResponseState<Login?>>()
    val fpDataLiveData: LiveData<ResponseState<Login?>>
        get() = _fpDataLiveData

    fun forgetPassword(loginData: LoginData) = viewModelScope.launch {
        _fpDataLiveData.value = fpRepository.forgotPassword(loginData)
    }


}