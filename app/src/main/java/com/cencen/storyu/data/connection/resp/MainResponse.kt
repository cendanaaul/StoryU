package com.cencen.storyu.data.connection.resp

import com.google.gson.annotations.SerializedName

data class MainResponse(
    @SerializedName("error")
    val error: Boolean?,
    @SerializedName("message")
    val message: String?,
)
