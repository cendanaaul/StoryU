package com.cencen.storyu.data.calls

import com.google.gson.annotations.SerializedName

data class SigninRequestCall(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)
