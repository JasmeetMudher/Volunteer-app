package com.volu.volunteerconnect.DI

import com.volu.volunteerconnect.API.VolunteerConnectApi
import com.volu.volunteerconnect.API.VolunteerConnectApi.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

//    val okHttpClient = OkHttpClient.Builder().addInterceptor(object : Interceptor {
//        override fun intercept(chain: Interceptor.Chain): Response {
////            val request = chain.request().newBuilder().addHeader("Authorization", )
//        }
//    })

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideVolunteerConnectApi(retrofit: Retrofit): VolunteerConnectApi =
        retrofit.create(VolunteerConnectApi::class.java)

}