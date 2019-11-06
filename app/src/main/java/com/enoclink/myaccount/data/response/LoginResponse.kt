package com.enoclink.myaccount.data.response

import com.squareup.moshi.Json


data class LoginResponse(
    @field:Json(name = "userid")
    var userId: String,
    var token: String
)
