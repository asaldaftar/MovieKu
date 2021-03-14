package com.irvan.movieku.mvvm.models

import com.google.gson.annotations.SerializedName

data class ReviewModel(
    @SerializedName("id")
    var id: String,
    @SerializedName("author")
    var author: String,
    @SerializedName("author_details")
    var authorDetails: AuthorModel? = null,
    @SerializedName("content")
    var content: String,
    @SerializedName("url")
    var url: String,
    @SerializedName("created_at")
    var createdAt: String,
    @SerializedName("updated_at")
    var updatedAt: String
)