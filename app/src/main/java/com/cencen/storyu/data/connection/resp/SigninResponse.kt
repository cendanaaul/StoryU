package com.cencen.storyu.data.connection.resp

import com.cencen.storyu.data.models.Signin
import com.google.gson.annotations.SerializedName

data class SigninResponse(
    @SerializedName("error")
    val error: Boolean?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("loginResult")
    val loginResult: Signin?
)
