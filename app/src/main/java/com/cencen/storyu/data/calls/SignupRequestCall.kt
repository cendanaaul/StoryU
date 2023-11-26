package com.cencen.storyu.data.calls

import com.google.gson.annotations.SerializedName

data class SignupRequestCall(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)
