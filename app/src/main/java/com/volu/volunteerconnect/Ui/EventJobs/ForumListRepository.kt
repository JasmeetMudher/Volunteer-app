package com.volu.volunteerconnect.Ui.EventJobs

import com.volu.volunteerconnect.API.VolunteerConnectApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForumListRepository @Inject constructor(
    private val api: VolunteerConnectApi
) {

    suspend fun getForumList(token: String) = api.getForumListById(token)

}