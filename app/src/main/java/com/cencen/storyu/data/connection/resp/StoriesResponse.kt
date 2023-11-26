package com.cencen.storyu.data.connection.resp

import com.cencen.storyu.data.models.RosterStory
import com.google.gson.annotations.SerializedName

data class StoriesResponse (
    @SerializedName("error")
    val error: Boolean?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("listStory")
    val listStory: List<RosterStory>
)