package com.irvan.movieku.mvvm.models

import com.google.gson.annotations.SerializedName

data class AuthorModel(
    @SerializedName("name")
    var name: String,
    @SerializedName("username")
    var username: String,
    @SerializedName("avatar_path")
    var avatar_path: String? = null,
    @SerializedName("rating")
    var rating: Int? = null
)