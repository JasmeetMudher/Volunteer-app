package com.volu.volunteerconnect.API

import com.volu.volunteerconnect.Model.Auth.Login
import com.volu.volunteerconnect.Model.Auth.LoginData
import com.volu.volunteerconnect.Model.Auth.RegisterData
import com.volu.volunteerconnect.Model.Category.CategoryResponse
import com.volu.volunteerconnect.Model.Events.Data
import com.volu.volunteerconnect.Model.Events.EventDataArrayModel
import com.volu.volunteerconnect.Model.Events.EventDataModel
import com.volu.volunteerconnect.Model.Forum.Forum
import com.volu.volunteerconnect.Model.Forum.ForumData
import com.volu.volunteerconnect.Model.Forum.ForumList
import com.volu.volunteerconnect.Model.Posts.PostsData
import com.volu.volunteerconnect.Model.Posts.PostsDataList
import com.volu.volunteerconnect.Model.Posts.PostsDataResponse
import com.volu.volunteerconnect.Model.Requests.RequestList
import com.volu.volunteerconnect.Model.Requests.Requests
import com.volu.volunteerconnect.Model.Requests.RequestsData
import com.volu.volunteerconnect.Model.User.UpateData
import com.volu.volunteerconnect.Model.User.UserDataModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface VolunteerConnectApi {

    companion object {
        const val BASE_URL = "https://volunteer-connect-eta.vercel.app/api/v1/"
    }

    /**
     * User Routes
     **/
    @Headers("Content-Type: Application/Json;charset=UTF-8")
    @POST("auth/login")
    suspend fun loginUser(
        @Body loginData: LoginData
    ): Response<Login?>

    @Headers("Content-Type: Application/Json;charset=UTF-8")
    @POST("auth/register")
    suspend fun registerUser(
        @Body registerBody: RegisterData
    ): Response<Login?>

    @Headers("Content-Type: Application/Json;charset=UTF-8")
    @POST("auth/forgotpassword")
    suspend fun forgotPassword(
        @Body loginData: LoginData
    ): Response<Login?>

    @GET("auth/me")
    suspend fun getLoggedInUser(
        @Header("Authorization") token: String
    ): Response<UserDataModel>

    @PUT("auth/updatedetails")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Body updateData: UpateData
    ): Response<UserDataModel>

    @GET("category")
    suspend fun getCategoryList(): Response<CategoryResponse>

    /**
     * Event Routes
     **/
    @Headers("Content-Type: Application/Json;charset=UTF-8")
    @POST("events")
    suspend fun createEvent(
        @Header("Authorization") token: String,
        @Body eventData: Data
    ): Response<EventDataModel>

    @GET("events")
    suspend fun getEventsList(): Response<EventDataArrayModel>

    @GET("events/{id}")
    suspend fun getEventsById(
        @Path(value = "id") id: String
    ): Response<EventDataModel>

    @Headers("Content-Type: Application/Json;charset=UTF-8")
    @POST("events")
    suspend fun updateEvent(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body eventData: Data,
    ): Response<EventDataModel>

    @GET("events/radius/{lat}/{long}/{radius}")
    suspend fun getEventsByDistance(
        @Path("lat") lat: String,
        @Path("long") long: String,
        @Path("radius") radius: String,
    ): Response<EventDataModel>

    @Multipart
    @Headers("Content-Type: Application/Json;charset=UTF-8")
    @PUT("events/{id}/photo")
    suspend fun uploadEventImage(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Part image: MultipartBody.Part,
    ): Response<EventDataModel>


    /**
     * Volunteer Request Routes
     **/
    @Headers("Content-Type: Application/Json;charset=UTF-8")
    @POST("volunteer/sendrequest")
    suspend fun sendVolunteerRequest(
        @Header("Authorization") token: String,
        @Body requestsBody: RequestsData
    ): Response<Requests>

    @Headers("Content-Type: Application/Json;charset=UTF-8")
    @PUT("volunteer/{id}")
    suspend fun handleVolunteerRequest(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body requestsBody: RequestsData
    ): Response<Requests>

    @GET("volunteer/recipientRequests/{id}")
    suspend fun getRequestsByRecipient(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<RequestList>

    @GET("volunteer/requesterRequests/{id}")
    suspend fun getRequestsByRequester(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<RequestList>


    /**
     * Forum Routes
     **/
    @Headers("Content-Type: Application/Json;charset=UTF-8")
    @POST("forum")
    suspend fun createForum(
        @Header("Authorization") token: String,
        @Body forumBody: ForumData
    ): Response<Forum>

    @Headers("Content-Type: Application/Json;charset=UTF-8")
    @GET("forum/{id}")
    suspend fun getForumById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Forum>

    @Headers("Content-Type: Application/Json;charset=UTF-8")
    @PUT("forum/{id}")
    suspend fun updateForum(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body forumBody: ForumData
    ): Response<Forum>

    @Headers("Content-Type: Application/Json;charset=UTF-8")
    @DELETE("forum/{id}")
    suspend fun deleteForum(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Forum>

    @Headers("Content-Type: Application/Json;charset=UTF-8")
    @GET("forum/forumList")
    suspend fun getForumListById(
        @Header("Authorization") token: String
    ): Response<ForumList>


    /**
     * Posts Routes
     **/

    @Headers("Content-Type: Application/Json;charset=UTF-8")
    @POST("posts")
    suspend fun createPost(
        @Header("Authorization") token: String,
        @Body postData: PostsData
    ): Response<PostsDataResponse>

    @Headers("Content-Type: Application/Json;charset=UTF-8")
    @PUT("posts/{id}")
    suspend fun updatePost(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body postData: PostsData
    ): Response<PostsDataResponse>

    @Headers("Content-Type: Application/Json;charset=UTF-8")
    @DELETE("posts/{id}")
    suspend fun deletePost(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<PostsDataList>

    @Headers("Content-Type: Application/Json;charset=UTF-8")
    @GET("posts/{id}")
    suspend fun getPostList(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<PostsDataList>


}