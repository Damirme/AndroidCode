package com.enoclink.myaccount.data

import com.enoclink.myaccount.data.model.LoggedInUser
import com.enoclink.myaccount.data.model.User
import com.enoclink.myaccount.data.response.AvatarResponse
import com.enoclink.myaccount.data.response.LoginResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response


class UserRepository(val dataSource: UserDataSource) {

    var user: LoggedInUser? = null
        private set

    var userProfile: User? = null
        private set

    init {
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    suspend fun login(username: String, password: String): Response<LoginResponse> {
        val result = dataSource.login(username, password).await()

        if (result.isSuccessful) {
            setLoggedInUser(result.body())
        }

        return result
    }

    private fun setLoggedInUser(loginResponse: LoginResponse?) {
        this.user = LoggedInUser(loginResponse?.userId ?: "", "")
        dataSource.token = loginResponse?.token
    }

    suspend fun getUserProfile(userId: String): Response<User> {
        val result = dataSource.getUser(userId, dataSource.token).await()

        if (result.isSuccessful) {
            saveLocallyUser(result.body())
        }

        return result
    }

    private fun saveLocallyUser(user: User?) {
        this.userProfile = user
    }

    fun setAvatar(userId: String, imageBase64: String): Deferred<Response<AvatarResponse>> {

        val avatarResponse = dataSource.setAvatar(userId, imageBase64, dataSource.token)


        return avatarResponse
    }
}
