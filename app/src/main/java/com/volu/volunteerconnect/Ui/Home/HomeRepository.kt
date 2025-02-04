package com.volu.volunteerconnect.Ui.Home

import com.volu.volunteerconnect.API.VolunteerConnectApi
import com.volu.volunteerconnect.Db.VolunteerConnectDatabase
import com.volu.volunteerconnect.Model.Events.DataFetch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    private val api: VolunteerConnectApi,
    private val db: VolunteerConnectDatabase
) {

    suspend fun getCurrentUser(token: String) = api.getLoggedInUser(token)
    suspend fun getCategoryList() = api.getCategoryList()


    fun getList() = db.volunteerDao().getAllEvents()
    suspend fun delete(data: DataFetch) = db.volunteerDao().delete(data)
    suspend fun insert(data: DataFetch) = db.volunteerDao().insert(data)
}