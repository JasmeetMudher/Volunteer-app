package com.volu.volunteerconnect.Ui.Register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volu.volunteerconnect.Model.Auth.RegisterData
import com.volu.volunteerconnect.Model.Auth.Login
import com.volu.volunteerconnect.Utils.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepository: RegisterRepository
): ViewModel() {

    private val _registerLiveData = MutableLiveData<ResponseState<Login?>>()
    val registerLiveData: LiveData<ResponseState<Login?>>
        get() = _registerLiveData


    fun register(registerData: RegisterData) = viewModelScope.launch {
        _registerLiveData.value = registerRepository.register(registerData)
    }

}