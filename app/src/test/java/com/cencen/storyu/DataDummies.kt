package com.cencen.storyu

import com.cencen.storyu.data.connection.resp.MainResponse
import com.cencen.storyu.data.connection.resp.SigninResponse
import com.cencen.storyu.data.connection.resp.StoriesResponse
import com.cencen.storyu.data.models.RosterStory
import com.cencen.storyu.data.models.Signin

object DataDummies {

    fun generatesDummySigninResponse(): SigninResponse {
        return SigninResponse(
            false, "token", Signin("id", "name", "token")
        )
    }

    fun generatesDummySignupResponse(): MainResponse {
        return MainResponse(
            false, "success"
        )
    }

    fun generatesDummyStory(): List<RosterStory> {
        val itemStory = arrayListOf<RosterStory>()
        for (a in 0 until 10) {
            val stories = RosterStory(
                "story-wjdwldkg",
                "mba taylor",
                "hai",
                "https://story-api.dicoding.dev/images/stories/photo-29-Oct-20233092927794738735338.jpg",
                "2023-10-29T12:50:49.720Z",
                -7.4501219272869665,
                112.45604096648879
            )
            itemStory.add(stories)
        }
        return itemStory
    }

    fun generatesDummyStoryLoc(): StoriesResponse {
        val itemStory: MutableList<RosterStory> = arrayListOf()
        for (a in 0 until 10) {
            val stories = RosterStory(
                "story-wjdwldkg",
                "mba taylor",
                "hai",
                "https://story-api.dicoding.dev/images/stories/photo-29-Oct-20233092927794738735338.jpg",
                "2023-10-29T12:50:49.720Z",
                -7.4501219272869665,
                112.45604096648879
            )
            itemStory.add(stories)
        }
        return StoriesResponse(false, "success", itemStory)
    }

    fun generatesDummyUploadStoryResponse(): MainResponse {
        return MainResponse(
            false, "success"
        )
    }
}