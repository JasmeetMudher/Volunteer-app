package com.volu.volunteerconnect.Ui.Requests

import com.volu.volunteerconnect.API.VolunteerConnectApi
import com.volu.volunteerconnect.Model.Requests.RequestsData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestRepository @Inject constructor(
    private val api: VolunteerConnectApi
) {

    suspend fun getRequestByRecipient(token: String, id: String) =
        api.getRequestsByRecipient(token, id)

    suspend fun getRequestByRequester(token: String, id: String) =
        api.getRequestsByRequester(token, id)

    suspend fun handleRequest(token: String, id: String, requestsData: RequestsData) =
        api.handleVolunteerRequest(token, id, requestsData)

//    suspend fun getEventsByUserId() = api.

}