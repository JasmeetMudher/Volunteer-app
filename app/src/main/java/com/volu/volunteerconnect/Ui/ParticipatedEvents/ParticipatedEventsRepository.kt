package com.volu.volunteerconnect.Ui.ParticipatedEvents

import com.volu.volunteerconnect.API.VolunteerConnectApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ParticipatedEventsRepository @Inject constructor(private val api: VolunteerConnectApi) {

    suspend fun getEventList() = api.getEventsList()
}