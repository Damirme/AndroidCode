package com.enoclink.myaccount.network.api

import com.enoclink.myaccount.data.model.User
import com.enoclink.myaccount.data.request.AvatarRequest
import com.enoclink.myaccount.data.request.LoginRequest
import com.enoclink.myaccount.data.response.AvatarResponse
import com.enoclink.myaccount.data.response.LoginResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface UserService {

    @POST("sessions/new")
    fun login(
        @Body request: LoginRequest
    ): Deferred<Response<LoginResponse>>

    @GET("users/{userid}")
    fun getUser(
        @Path("userid") userId: String
    ): Deferred<Response<User>>

    @POST("users/{userid}/avatar")
    fun setAvatar(
        @Path("userid") userId: String,
        @Body avatarRequest: AvatarRequest
    ): Deferred<Response<AvatarResponse>>
}