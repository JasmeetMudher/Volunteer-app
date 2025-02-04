package com.volu.volunteerconnect.Ui.Login

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
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _signInDataLiveData = MutableLiveData<ResponseState<Login?>>()
    val signInDataLiveData: LiveData<ResponseState<Login?>>
        get() = _signInDataLiveData

    fun loginUser(loginData: LoginData) = viewModelScope.launch {
        _signInDataLiveData.value = loginRepository.login(loginData)
    }
}