package com.irvan.movieku.apis.apiresponses

import com.google.gson.annotations.SerializedName
import com.irvan.movieku.mvvm.models.MovieModel


data class MovieListResponse(
    @SerializedName("page")
    var page: Int,
    @SerializedName("dates")
    val dates: Date? = null,
    @SerializedName("results")
    val results: MutableList<MovieModel>,
    @SerializedName("total_pages")
    val total_pages: Int = 0,
    @SerializedName("total_results")
    val total_results: Int = 0
) {
    data class Date(
        @SerializedName("maximum")
        val maximum: String,
        @SerializedName("minimum")
        val minimum: String
    )
}