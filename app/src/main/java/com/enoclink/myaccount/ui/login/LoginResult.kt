package com.enoclink.myaccount.ui.login

import com.enoclink.myaccount.data.response.LoginResponse

data class LoginResult(
    val success: LoginResponse? = null,
    val error: Int? = null
)
