package com.irvan.movieku.apis.apiresponses

import com.google.gson.annotations.SerializedName
import com.irvan.movieku.mvvm.models.VideoModel

data class VideoResponse(
    @SerializedName("id")
    var id: Int,
    @SerializedName("results")
    val results: MutableList<VideoModel>
)