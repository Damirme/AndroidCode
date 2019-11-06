package com.enoclink.myaccount.data

import com.enoclink.myaccount.data.model.User
import com.enoclink.myaccount.data.request.AvatarRequest
import com.enoclink.myaccount.data.request.LoginRequest
import com.enoclink.myaccount.data.response.AvatarResponse
import com.enoclink.myaccount.data.response.LoginResponse
import com.enoclink.myaccount.network.RetrofitClientInstance
import com.enoclink.myaccount.network.api.UserService
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Response

class UserDataSource {

    var token: String? = null
        set(value) {
            RetrofitClientInstance.recreateInstance(value)
        }

    fun login(email: String, password: String): Deferred<Response<LoginResponse>> {
        return try {
            var loginRequest = LoginRequest(email, password)

            //todo change it to API call
//            return RetrofitClientInstance.retrofitInstance().create(UserService::class.java).login(loginRequest)

            val deferred = CompletableDeferred<Response<LoginResponse>>()
            deferred.complete(Response.success(LoginResponse("123231", "Token")))
            return deferred
        } catch (e: Throwable) {
            e.printStackTrace()
            val deferred = CompletableDeferred<Response<LoginResponse>>()
            deferred.complete(Response.error(500, ResponseBody.create(null, "")))
            return deferred
        }
    }

    fun logout() {

    }

    fun getUser(userId: String, token: String?): Deferred<Response<User>> {
        return try {

//            return RetrofitClientInstance.retrofitInstance().create(UserService::class.java).getUserProfile(userId)

            val deferred = CompletableDeferred<Response<User>>()
            deferred.complete(
                Response.success(
                    User(
                        "damirmailybayev@gmail.com",
                        "https://www.gravatar.com/avatar/69a27c94951701e0a400db61273d422c"
                    )
                )
            )
            return deferred
        } catch (e: Throwable) {
            e.printStackTrace()
            val deferred = CompletableDeferred<Response<User>>()
            deferred.complete(Response.error(500, ResponseBody.create(null, "")))
            return deferred
        }
    }

    fun setAvatar(userId: String, imageBase64: String, token: String?): Deferred<Response<AvatarResponse>> {
        return try {
            var avatarRequest = AvatarRequest(imageBase64)
            return RetrofitClientInstance.retrofitInstance()
                .create(UserService::class.java)
                .setAvatar(userId, avatarRequest)
        } catch (e: Throwable) {
            e.printStackTrace()
            val deferred = CompletableDeferred<Response<AvatarResponse>>()
            deferred.complete(Response.error(500, ResponseBody.create(null, "")))
            return deferred
        }
    }
}

