package com.cencen.storyu.data.models

import com.google.gson.annotations.SerializedName

data class Signin(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("token")
    val token: String?
)