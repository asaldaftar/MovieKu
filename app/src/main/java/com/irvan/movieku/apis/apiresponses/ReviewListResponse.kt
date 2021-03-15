package com.irvan.movieku.apis.apiresponses

import com.google.gson.annotations.SerializedName
import com.irvan.movieku.mvvm.models.ReviewModel

data class ReviewListResponse(
    @SerializedName("id")
    var id: Int,
    @SerializedName("page")
    var page: Int,
    @SerializedName("results")
    val results: MutableList<ReviewModel>,
    @SerializedName("total_pages")
    val total_pages: Int = 0,
    @SerializedName("total_results")
    val total_results: Int = 0
)