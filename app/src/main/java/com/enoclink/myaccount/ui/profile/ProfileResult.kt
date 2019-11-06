package com.enoclink.myaccount.ui.profile

import com.enoclink.myaccount.data.model.User
import com.enoclink.myaccount.data.response.LoginResponse

data class ProfileResult(
    val success: User? = null,
    val error: Int? = null
)
