package com.volu.volunteerconnect.Ui.Home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volu.volunteerconnect.Model.Category.CategoryResponse
import com.volu.volunteerconnect.Model.Events.DataFetch
import com.volu.volunteerconnect.Model.User.UserDataModel
import com.volu.volunteerconnect.Utils.ResponseState
import com.volu.volunteerconnect.Utils.errorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    val isNetworkConnectedLiveData = MutableLiveData<Boolean>()
    private val _categoryDataLiveData = MutableLiveData<ResponseState<CategoryResponse?>>()
    val categoryDataLiveData: LiveData<ResponseState<CategoryResponse?>>
        get() = _categoryDataLiveData

    private val _userLiveData = MutableLiveData<ResponseState<UserDataModel?>>()
    val userLiveData: LiveData<ResponseState<UserDataModel?>>
        get() = _userLiveData

    fun deleteEvent(data: DataFetch) = viewModelScope.launch {
        homeRepository.delete(data)
    }

    fun insertEvent(data: DataFetch) = viewModelScope.launch {
        homeRepository.insert(data)
    }

    fun savedEvents() = homeRepository.getList()

    fun getCurrentLoggedinUser(token: String) = viewModelScope.launch {
        if (isNetworkConnectedLiveData.value == false) {
            _userLiveData.value =
                ResponseState.Error("This app requires an active internet connection to be used.")
        }
        _userLiveData.value = ResponseState.Loading()
        try {
            val response = homeRepository.getCurrentUser(token)
            if (response.isSuccessful && response.body()?.success == true) {
                _userLiveData.value = ResponseState.Success(response.body())
            } else {
                _userLiveData.value =
                    ResponseState.Error(errorResponse(response)?.error.toString())
            }
        } catch (e: HttpException) {
            _userLiveData.value =
                ResponseState.Error("Something went wrong. Please try again later.")
        } catch (e: IOException) {
            _userLiveData.value =
                ResponseState.Error("Couldn\'t reach server. Check your internet connection.")
        }
    }


    fun getCategoryList() = viewModelScope.launch {
        if (isNetworkConnectedLiveData.value == false) {
            _categoryDataLiveData.value =
                ResponseState.Error("This app requires an active internet connection to be used.")
        }
        _categoryDataLiveData.value = ResponseState.Loading()
        try {
            val response = homeRepository.getCategoryList()
            if (response.isSuccessful && response.body()?.success == true) {
                _categoryDataLiveData.value = ResponseState.Success(response.body())
            } else {
                _categoryDataLiveData.value =
                    ResponseState.Error(errorResponse(response)?.error.toString())
            }
        } catch (e: HttpException) {
            _categoryDataLiveData.value =
                ResponseState.Error("Something went wrong. Please try again later.")
        } catch (e: IOException) {
            _categoryDataLiveData.value =
                ResponseState.Error("Couldn\'t reach server. Check your internet connection.")
        }
    }

}